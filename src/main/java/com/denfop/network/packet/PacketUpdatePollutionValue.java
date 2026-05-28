package com.denfop.network.packet;

import com.denfop.IUCore;
import com.denfop.api.pollution.PollutionManager;
import com.denfop.api.pollution.client.PollutionClientRenderRefresh;
import com.denfop.api.pollution.component.ChunkLevel;
import com.denfop.api.pollution.component.LevelPollution;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;

public class PacketUpdatePollutionValue implements IPacket {

    public PacketUpdatePollutionValue() {
    }

    public PacketUpdatePollutionValue(ChunkPos pos, double pollution, EnumPollutionSyncType type) {
        PollutionManager manager = PollutionManager.pollutionManager;
        if (manager == null) {
            return;
        }

        ChunkLevel chunkLevel = type == EnumPollutionSyncType.AIR
                ? manager.getChunkLevelAir(pos)
                : manager.getChunkLevelSoil(pos);

        if (chunkLevel == null) {
            chunkLevel = new ChunkLevel(pos, LevelPollution.VERY_LOW, 0);
            if (type == EnumPollutionSyncType.AIR) {
                manager.addChunkLevelAir(chunkLevel);
            } else {
                manager.addChunkLevelSoil(chunkLevel);
            }
        }

        chunkLevel.addPollution(pollution);

        CustomPacketBuffer buffer = new CustomPacketBuffer(128);
        buffer.writeByte(this.getId());
        PollutionPacketIO.writeType(buffer, type);
        PollutionPacketIO.writeChunkLevel(buffer, chunkLevel);
        buffer.flip();
        IUCore.network.getServer().sendPacket(buffer);
    }

    @Override
    public byte getId() {
        return 105;
    }

    @Override
    public void readPacket(final CustomPacketBuffer customPacketBuffer, final Player entityPlayer) {
        EnumPollutionSyncType type = PollutionPacketIO.readType(customPacketBuffer);
        ChunkLevel chunkLevel = PollutionPacketIO.readChunkLevel(customPacketBuffer);
        PollutionPacketIO.applyChunkLevel(chunkLevel, type);
        PollutionClientRenderRefresh.onSingleChunkPollutionUpdated(chunkLevel.getPos());
    }

    @Override
    public EnumTypePacket getPacketType() {
        return EnumTypePacket.SERVER;
    }
}