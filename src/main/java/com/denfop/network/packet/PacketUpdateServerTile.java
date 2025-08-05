package com.denfop.network.packet;

import com.denfop.IUCore;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.io.IOException;

public class PacketUpdateServerTile implements IPacket {

    private CustomPacketBuffer buffer;

    public PacketUpdateServerTile() {

    }

    public PacketUpdateServerTile(BlockEntity te, double event) {
        CustomPacketBuffer buffer = new CustomPacketBuffer(32, te.getLevel().registryAccess());
        buffer.writeByte(this.getId());
        try {
            EncoderHandler.encode(buffer, te, false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        buffer.writeDouble(event);
        buffer.flip();
        this.buffer = buffer;
        IUCore.network.getClient().sendPacket(this, buffer);
    }

    @Override
    public CustomPacketBuffer getPacketBuffer() {
        return buffer;
    }

    @Override
    public void setPacketBuffer(CustomPacketBuffer customPacketBuffer) {
        buffer = customPacketBuffer;
    }

    @Override
    public byte getId() {
        return 5;
    }

    @Override
    public void readPacket(final CustomPacketBuffer is, final Player entityPlayer) {
        final Object teDeferred;
        try {
            teDeferred = DecoderHandler.decodeDeferred(is, BlockEntity.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        final double event = is.readDouble();
        BlockEntity te = DecoderHandler.getValue(teDeferred);
        if (te instanceof IUpdatableTileEvent) {
            ((IUpdatableTileEvent) te).updateTileServer(entityPlayer, event);
        }
    }

    @Override
    public EnumTypePacket getPacketType() {
        return EnumTypePacket.CLIENT;
    }


}
