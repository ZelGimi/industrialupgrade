package com.denfop.network.packet;

import com.denfop.api.pollution.PollutionManager;
import com.denfop.api.pollution.component.ChunkLevel;

public final class PollutionPacketIO {

    private PollutionPacketIO() {
    }

    public static void writeType(CustomPacketBuffer buffer, EnumPollutionSyncType type) {
        buffer.writeByte(type.getId());
    }

    public static EnumPollutionSyncType readType(CustomPacketBuffer buffer) {
        return EnumPollutionSyncType.byId(buffer.readByte());
    }

    public static void writeChunkLevel(CustomPacketBuffer buffer, ChunkLevel chunkLevel) {
        buffer.writeDouble(chunkLevel.getPollution());
        buffer.writeInt(chunkLevel.getLevelPollution().ordinal());
        buffer.writeInt(chunkLevel.getPos().x);
        buffer.writeInt(chunkLevel.getPos().z);
        buffer.writeInt(chunkLevel.getDefaultPos().x);
        buffer.writeInt(chunkLevel.getDefaultPos().z);
    }

    public static ChunkLevel readChunkLevel(CustomPacketBuffer buffer) {
        return new ChunkLevel(buffer);
    }

    public static void applyChunkLevel(ChunkLevel chunkLevel, EnumPollutionSyncType type) {
        PollutionManager manager = PollutionManager.pollutionManager;
        if (manager == null || chunkLevel == null) {
            return;
        }

        if (type == EnumPollutionSyncType.AIR) {
            manager.addChunkLevelAir(chunkLevel);
        } else {
            manager.addChunkLevelSoil(chunkLevel);
        }
    }
}