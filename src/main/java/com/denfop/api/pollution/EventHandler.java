package com.denfop.api.pollution;

import com.denfop.api.pollution.air.PollutionAirLoadEvent;
import com.denfop.api.pollution.air.PollutionAirUnLoadEvent;
import com.denfop.api.pollution.layer.EnvironmentalLayerManager;
import com.denfop.api.pollution.soil.PollutionSoilLoadEvent;
import com.denfop.api.pollution.soil.PollutionSoilUnLoadEvent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EventHandler {

    private static final String LAST_SYNC_CHUNK_X = "iu_pollution_sync_chunk_x";
    private static final String LAST_SYNC_CHUNK_Z = "iu_pollution_sync_chunk_z";
    private static final String LAST_SYNC_TIME = "iu_pollution_sync_time";

    @SubscribeEvent
    public void tick(TickEvent.PlayerTickEvent event) {
        if (event.player.getLevel().dimension() != Level.OVERWORLD || event.player.getLevel().isClientSide || event.phase == TickEvent.Phase.START) {
            return;
        }
        PollutionManager.pollutionManager.work(event.player);
        syncPlayerSnapshotIfNeeded(event.player, false);
    }

    @SubscribeEvent
    public void tick(TickEvent.LevelTickEvent event) {
        if (event.level.dimension() != Level.OVERWORLD || event.level.isClientSide || event.phase == TickEvent.Phase.START) {
            return;
        }
        PollutionManager.pollutionManager.tick(event.level);
        PollutionManager.pollutionManager.processSoilTerrain((ServerLevel) event.level);
        PollutionManager.pollutionManager.processVegetationDecay((ServerLevel) event.level);
        PollutionManager.pollutionManager.processLeafDecay((ServerLevel) event.level);
        EnvironmentalLayerManager.tick((ServerLevel) event.level);
    }

    @SubscribeEvent
    public void tick(PollutionAirLoadEvent event) {
        if (((Level) event.getLevel()).dimension() != Level.OVERWORLD || ((Level) event.getLevel()).isClientSide) {
            return;
        }
        PollutionManager.pollutionManager.addAirPollutionMechanism(event.tile);
    }

    @SubscribeEvent
    public void tick(PollutionAirUnLoadEvent event) {
        if (((Level) event.getLevel()).dimension() != Level.OVERWORLD || ((Level) event.getLevel()).isClientSide) {
            return;
        }
        PollutionManager.pollutionManager.removeAirPollutionMechanism(event.tile);
    }

    @SubscribeEvent
    public void tick(PollutionSoilLoadEvent event) {
        if (((Level) event.getLevel()).dimension() != Level.OVERWORLD || ((Level) event.getLevel()).isClientSide) {
            return;
        }
        PollutionManager.pollutionManager.addSoilPollutionMechanism(event.tile);
    }

    @SubscribeEvent
    public void tick(PollutionSoilUnLoadEvent event) {
        if (((Level) event.getLevel()).dimension() != Level.OVERWORLD || ((Level) event.getLevel()).isClientSide) {
            return;
        }
        PollutionManager.pollutionManager.removeSoilPollutionMechanism(event.tile);
    }

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity().getLevel().isClientSide || event.getEntity().getLevel().dimension() != Level.OVERWORLD) {
            return;
        }
        clearSyncMarkers(event.getEntity());
        syncPlayerSnapshotIfNeeded(event.getEntity(), true);
    }

    @SubscribeEvent
    public void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (event.getEntity().getLevel().isClientSide || event.getEntity().getLevel().dimension() != Level.OVERWORLD) {
            return;
        }
        clearSyncMarkers(event.getEntity());
        syncPlayerSnapshotIfNeeded(event.getEntity(), true);
    }

    private void syncPlayerSnapshotIfNeeded(Player player, boolean force) {
        if (player == null || player.getLevel().isClientSide || player.getLevel().dimension() != Level.OVERWORLD) {
            return;
        }

        CompoundTag tag = player.getPersistentData();
        ChunkPos currentChunk = new ChunkPos(player.blockPosition());
        long gameTime = player.getLevel().getGameTime();

        boolean chunkChanged = !tag.contains(LAST_SYNC_CHUNK_X)
                || !tag.contains(LAST_SYNC_CHUNK_Z)
                || tag.getInt(LAST_SYNC_CHUNK_X) != currentChunk.x
                || tag.getInt(LAST_SYNC_CHUNK_Z) != currentChunk.z;

        boolean periodicRefresh = !tag.contains(LAST_SYNC_TIME)
                || (gameTime - tag.getLong(LAST_SYNC_TIME)) >= 200L;

        if (force || chunkChanged || periodicRefresh) {
            PollutionManager.pollutionManager.update(player);
            tag.putInt(LAST_SYNC_CHUNK_X, currentChunk.x);
            tag.putInt(LAST_SYNC_CHUNK_Z, currentChunk.z);
            tag.putLong(LAST_SYNC_TIME, gameTime);
        }
    }

    private void clearSyncMarkers(Player player) {
        CompoundTag tag = player.getPersistentData();
        tag.remove(LAST_SYNC_CHUNK_X);
        tag.remove(LAST_SYNC_CHUNK_Z);
        tag.remove(LAST_SYNC_TIME);
    }
}