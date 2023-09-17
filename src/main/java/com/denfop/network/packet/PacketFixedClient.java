package com.denfop.network.packet;

import com.denfop.IUCore;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.tiles.base.TileEntityBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.io.IOException;

public class PacketFixedClient implements IPacket{
    public PacketFixedClient(){

    }
    public PacketFixedClient(World world, BlockPos pos){
        CustomPacketBuffer buffer = new CustomPacketBuffer();
        buffer.writeByte(this.getId());
        buffer.writeBlockPos(pos);
        try {
            EncoderHandler.encode(buffer,world);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        IUCore.network.getClient().sendPacket(buffer);
    }
    @Override
    public byte getId() {
        return 50;
    }

    @Override
    public void readPacket(final CustomPacketBuffer customPacketBuffer, final EntityPlayer entityPlayer) {
        BlockPos pos = customPacketBuffer.readBlockPos();
        World world;
        try {
             world = (World) DecoderHandler.decode(customPacketBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if(world != null){
            if(world.getTileEntity(pos) != null){
                new PacketUpdateTile((TileEntityBlock) world.getTileEntity(pos));
            }
        }
    }

    @Override
    public EnumTypePacket getPacketType() {
        return EnumTypePacket.CLIENT;
    }

}
