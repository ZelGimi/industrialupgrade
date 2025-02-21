package com.denfop.network.packet;

import com.denfop.IUCore;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.tiles.base.TileEntityBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;

import java.io.IOException;

public class PacketUpdateTe implements IPacket {

    public PacketUpdateTe() {
    }

    ;

    public PacketUpdateTe(World world, BlockPos pos) {
        CustomPacketBuffer buffer = new CustomPacketBuffer();
        buffer.writeByte(this.getId());
        buffer.writeBlockPos(pos);
        try {
            EncoderHandler.encode(buffer, world);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        IUCore.network.getClient().sendPacket(buffer);
    }

    @Override
    public byte getId() {
        return 111;
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
        if (world != null) {
            if (world.getTileEntity(pos) != null) {
                TileEntityBlock block = (TileEntityBlock) world.getTileEntity(pos);
                final Chunk chunk = world.getChunkFromBlockCoords(pos);
                ExtendedBlockStorage extendedblockstorage = chunk.storageArrays[pos.getY() >> 4];
                int i = pos.getX() & 15;
                int j = pos.getY();
                int k = pos.getZ() & 15;
                extendedblockstorage.set(i, j & 15, k, block.getBlockState());
            }
        }
    }

    @Override
    public EnumTypePacket getPacketType() {
        return EnumTypePacket.CLIENT;
    }

}
