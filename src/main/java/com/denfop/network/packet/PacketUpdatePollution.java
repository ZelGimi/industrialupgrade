package com.denfop.network.packet;

import com.denfop.IUCore;
import com.denfop.api.pollution.client.PollutionClientRenderRefresh;
import com.denfop.api.pollution.component.ChunkLevel;
import net.minecraft.world.entity.player.Player;

public class PacketUpdatePollution implements IPacket {

    public PacketUpdatePollution() {
    }

    public PacketUpdatePollution(ChunkLevel chunkLevel, EnumPollutionSyncType type) {
        CustomPacketBuffer buffer = new CustomPacketBuffer(128);
        buffer.writeByte(this.getId());
        PollutionPacketIO.writeType(buffer, type);
        PollutionPacketIO.writeChunkLevel(buffer, chunkLevel);
        buffer.flip();
        IUCore.network.getServer().sendPacket(buffer);
    }

    @Override
    public byte getId() {
        return 104;
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