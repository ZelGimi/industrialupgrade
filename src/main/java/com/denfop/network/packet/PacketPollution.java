package com.denfop.network.packet;

import com.denfop.IUCore;
import com.denfop.api.pollution.PollutionManager;
import com.denfop.api.pollution.client.PollutionClientRenderRefresh;
import com.denfop.api.pollution.component.ChunkLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;

import java.util.*;

public class PacketPollution implements IPacket {

    private CustomPacketBuffer buffer;

    public PacketPollution() {
    }

    public PacketPollution(Collection<ChunkLevel> airPollution, Collection<ChunkLevel> soilPollution, Player player) {
        CustomPacketBuffer buffer = new CustomPacketBuffer(player.registryAccess());
        buffer.writeByte(this.getId());

        buffer.writeInt(airPollution.size());
        for (ChunkLevel chunkLevel : airPollution) {
            PollutionPacketIO.writeChunkLevel(buffer, chunkLevel);
        }

        buffer.writeInt(soilPollution.size());
        for (ChunkLevel chunkLevel : soilPollution) {
            PollutionPacketIO.writeChunkLevel(buffer, chunkLevel);
        }

        buffer.flip();
        this.buffer = buffer;
        IUCore.network.getServer().sendPacket(this, buffer, (ServerPlayer) player);
    }

    private static void collectChanges(
            Map<ChunkPos, ChunkLevel> oldMap,
            Map<ChunkPos, ChunkLevel> newMap,
            Set<ChunkPos> out
    ) {
        Set<ChunkPos> keys = new HashSet<>(oldMap.keySet());
        keys.addAll(newMap.keySet());

        for (ChunkPos pos : keys) {
            ChunkLevel oldValue = oldMap.get(pos);
            ChunkLevel newValue = newMap.get(pos);

            if (!sameChunkLevel(oldValue, newValue)) {
                out.add(pos);
            }
        }
    }

    private static boolean sameChunkLevel(ChunkLevel a, ChunkLevel b) {
        if (a == b) {
            return true;
        }
        if (a == null || b == null) {
            return false;
        }

        if (!a.getPos().equals(b.getPos())) {
            return false;
        }
        if (!a.getDefaultPos().equals(b.getDefaultPos())) {
            return false;
        }
        if (a.getLevelPollution() != b.getLevelPollution()) {
            return false;
        }

        return Math.abs(a.getPollution() - b.getPollution()) < 0.0001D;
    }

    @Override
    public byte getId() {
        return 102;
    }

    @Override
    public CustomPacketBuffer getPacketBuffer() {
        return this.buffer;
    }

    @Override
    public void setPacketBuffer(CustomPacketBuffer customPacketBuffer) {
        this.buffer = customPacketBuffer;
    }

    @Override
    public void readPacket(final CustomPacketBuffer customPacketBuffer, final Player entityPlayer) {
        PollutionManager manager = PollutionManager.pollutionManager;
        if (manager == null) {
            int airSizeSkip = customPacketBuffer.readInt();
            for (int i = 0; i < airSizeSkip; i++) {
                PollutionPacketIO.readChunkLevel(customPacketBuffer);
            }

            int soilSizeSkip = customPacketBuffer.readInt();
            for (int i = 0; i < soilSizeSkip; i++) {
                PollutionPacketIO.readChunkLevel(customPacketBuffer);
            }
            return;
        }

        Map<ChunkPos, ChunkLevel> oldAir = new HashMap<>(manager.getPollutionAirMap());
        Map<ChunkPos, ChunkLevel> oldSoil = new HashMap<>(manager.getPollutionSoilMap());

        Map<ChunkPos, ChunkLevel> newAir = new HashMap<>();
        Map<ChunkPos, ChunkLevel> newSoil = new HashMap<>();

        int airSize = customPacketBuffer.readInt();
        for (int i = 0; i < airSize; i++) {
            ChunkLevel chunkLevel = PollutionPacketIO.readChunkLevel(customPacketBuffer);
            newAir.put(chunkLevel.getPos(), chunkLevel);
        }

        int soilSize = customPacketBuffer.readInt();
        for (int i = 0; i < soilSize; i++) {
            ChunkLevel chunkLevel = PollutionPacketIO.readChunkLevel(customPacketBuffer);
            newSoil.put(chunkLevel.getPos(), chunkLevel);
        }

        manager.clearSyncedPollution();
        for (ChunkLevel chunkLevel : newAir.values()) {
            manager.addChunkLevelAir(chunkLevel);
        }
        for (ChunkLevel chunkLevel : newSoil.values()) {
            manager.addChunkLevelSoil(chunkLevel);
        }

        Set<ChunkPos> changedChunks = new HashSet<>();
        collectChanges(oldAir, newAir, changedChunks);
        collectChanges(oldSoil, newSoil, changedChunks);

        PollutionClientRenderRefresh.onFullPollutionSnapshotApplied(changedChunks);
    }

    @Override
    public EnumTypePacket getPacketType() {
        return EnumTypePacket.SERVER;
    }
}