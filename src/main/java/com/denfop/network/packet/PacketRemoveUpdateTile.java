package com.denfop.network.packet;

import com.denfop.IUCore;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.tiles.base.TileEntityBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.io.IOException;

public class PacketRemoveUpdateTile implements IPacket{
    public PacketRemoveUpdateTile(){

    }
    public PacketRemoveUpdateTile(TileEntityBlock te){
        CustomPacketBuffer buffer = new CustomPacketBuffer(64);
        buffer.writeByte(this.getId());

        try {
            EncoderHandler.encode(buffer,te.getWorld());
            EncoderHandler.encode(buffer,te.getPos());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        IUCore.network.getServer().sendPacket(buffer);
    }
    @Override
    public byte getId() {
        return 60;
    }

    @Override
    public void readPacket(final CustomPacketBuffer is, final EntityPlayer entityPlayer) {
        World world1;
        try {
             world1 = (World) DecoderHandler.decode(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        BlockPos pos;
        try {
            pos = (BlockPos) DecoderHandler.decode(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        byte[] bytes = new byte[is.writerIndex() - is.readerIndex()];
        is.readBytes(bytes);
        if (!(is.readerIndex() < is.writerIndex())) {
            final World finalWorld = world1;
            IUCore.proxy.requestTick(false, () -> {
                World world = IUCore.proxy.getPlayerWorld();
                if (world != null && finalWorld != null && world.provider.getDimension() == finalWorld.provider.getDimension()) {
                    world.removeTileEntity(pos);
                    world.setBlockToAir(pos);
                }
            });
        }
    }

    @Override
    public EnumTypePacket getPacketType() {
        return EnumTypePacket.SERVER;
    }

}
