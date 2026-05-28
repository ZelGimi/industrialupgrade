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
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

public class EventHandler {

    private static final String LAST_SYNC_CHUNK_X = "iu_pollution_sync_chunk_x";
    private static final String LAST_SYNC_CHUNK_Z = "iu_pollution_sync_chunk_z";
    private static final String LAST_SYNC_TIME = "iu_pollution_sync_time";

    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();

        if (player.level().dimension() != Level.OVERWORLD || player.level().isClientSide) {
            return;
        }

        PollutionManager.pollutionManager.work(player);
        syncPlayerSnapshotIfNeeded(player, false);
    }

    @SubscribeEvent
    public void onLevelTick(LevelTickEvent.Post event) {
        Level level = event.getLevel();

        if (level.dimension() != Level.OVERWORLD || level.isClientSide) {
            return;
        }

        ServerLevel serverLevel = (ServerLevel) level;

        PollutionManager.pollutionManager.tick(level);
        PollutionManager.pollutionManager.processSoilTerrain(serverLevel);
        PollutionManager.pollutionManager.processVegetationDecay(serverLevel);
        PollutionManager.pollutionManager.processLeafDecay(serverLevel);
        EnvironmentalLayerManager.tick(serverLevel);
    }

    @SubscribeEvent
    public void onPollutionAirLoad(PollutionAirLoadEvent event) {
        Level level = (Level) event.getLevel();
        if (level.dimension() != Level.OVERWORLD || level.isClientSide) {
            return;
        }
        PollutionManager.pollutionManager.addAirPollutionMechanism(event.tile);
    }

    @SubscribeEvent
    public void onPollutionAirUnload(PollutionAirUnLoadEvent event) {
        Level level = (Level) event.getLevel();
        if (level.dimension() != Level.OVERWORLD || level.isClientSide) {
            return;
        }
        PollutionManager.pollutionManager.removeAirPollutionMechanism(event.tile);
    }

    @SubscribeEvent
    public void onPollutionSoilLoad(PollutionSoilLoadEvent event) {
        Level level = (Level) event.getLevel();
        if (level.dimension() != Level.OVERWORLD || level.isClientSide) {
            return;
        }
        PollutionManager.pollutionManager.addSoilPollutionMechanism(event.tile);
    }

    @SubscribeEvent
    public void onPollutionSoilUnload(PollutionSoilUnLoadEvent event) {
        Level level = (Level) event.getLevel();
        if (level.dimension() != Level.OVERWORLD || level.isClientSide) {
            return;
        }
        PollutionManager.pollutionManager.removeSoilPollutionMechanism(event.tile);
    }

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();

        if (player.level().isClientSide || player.level().dimension() != Level.OVERWORLD) {
            return;
        }

        clearSyncMarkers(player);
        syncPlayerSnapshotIfNeeded(player, true);
    }

    @SubscribeEvent
    public void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        Player player = event.getEntity();

        if (player.level().isClientSide || player.level().dimension() != Level.OVERWORLD) {
            return;
        }

        clearSyncMarkers(player);
        syncPlayerSnapshotIfNeeded(player, true);
    }

    private void syncPlayerSnapshotIfNeeded(Player player, boolean force) {
        if (player == null || player.level().isClientSide || player.level().dimension() != Level.OVERWORLD) {
            return;
        }

        CompoundTag tag = player.getPersistentData();
        ChunkPos currentChunk = new ChunkPos(player.blockPosition());
        long gameTime = player.level().getGameTime();

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