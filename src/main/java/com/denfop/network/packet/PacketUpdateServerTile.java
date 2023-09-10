package com.denfop.network.packet;

import com.denfop.IUCore;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.network.IUpdatableTileEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

import java.io.IOException;

public class PacketUpdateServerTile implements IPacket {

    public PacketUpdateServerTile() {

    }

    public PacketUpdateServerTile(TileEntity te, double event) {
        CustomPacketBuffer buffer = new CustomPacketBuffer(32);
        buffer.writeByte(this.getId());
        try {
            EncoderHandler.encode(buffer, te, false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        buffer.writeDouble(event);
        buffer.flip();
        IUCore.network.getClient().sendPacket(buffer);
    }

    @Override
    public byte getId() {
        return 5;
    }

    @Override
    public void readPacket(final CustomPacketBuffer is, final EntityPlayer entityPlayer) {
        final Object teDeferred;
        try {
            teDeferred = DecoderHandler.decodeDeferred(is, TileEntity.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        final double event = is.readDouble();
        IUCore.proxy.requestTick(true, () -> {
            TileEntity te = DecoderHandler.getValue(teDeferred);
            if (te instanceof IUpdatableTileEvent) {
                ((IUpdatableTileEvent) te).updateTileServer(entityPlayer, event);
            }

        });
    }

    @Override
    public EnumTypePacket getPacketType() {
        return EnumTypePacket.CLIENT;
    }


}
