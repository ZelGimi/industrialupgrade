package com.denfop.network.packet;

import com.denfop.IUCore;
import com.denfop.api.pollution.client.PollutionClientRenderRefresh;
import com.denfop.api.pollution.component.ChunkLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class PacketUpdatePollution implements IPacket {


    private CustomPacketBuffer buffer;

    public PacketUpdatePollution() {

    }

    public PacketUpdatePollution(Level world, ChunkLevel chunkLevel, EnumPollutionSyncType type) {
        CustomPacketBuffer buffer = new CustomPacketBuffer(128, world.registryAccess());
        buffer.writeByte(this.getId());
        PollutionPacketIO.writeType(buffer, type);
        PollutionPacketIO.writeChunkLevel(buffer, chunkLevel);
        buffer.flip();
        this.buffer = buffer;
        IUCore.network.getServer().sendPacket(this, buffer);
    }

    @Override
    public CustomPacketBuffer getPacketBuffer() {
        return buffer;
    }

    @Override
    public void setPacketBuffer(CustomPacketBuffer customPacketBuffer) {
        this.buffer = customPacketBuffer;
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