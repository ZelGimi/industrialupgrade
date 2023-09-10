package com.denfop.network.packet;

import com.denfop.IUCore;
import com.denfop.blocks.TileBlockCreator;
import com.denfop.network.DecoderHandler;
import com.denfop.tiles.base.TileEntityBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.io.IOException;

public class PacketUpdateTile implements IPacket {

    public PacketUpdateTile() {

    }

    public PacketUpdateTile(TileEntityBlock te) {

        IUCore.network.getServer().addTileToUpdate(te);

    }

    public PacketUpdateTile(CustomPacketBuffer data, EntityPlayerMP player) {
        CustomPacketBuffer buffer = new CustomPacketBuffer(16384);
        buffer.writeByte(getId());
        buffer.writeBytes(data);
        IUCore.network.getServer().sendPacket(buffer, player);
    }

    private static void apply(BlockPos pos, Class<? extends TileEntityBlock> teClass, World world, byte[] is) {
        if (world.isBlockLoaded(pos, false)) {
            TileEntity te = world.getTileEntity(pos);
            if (teClass != null && (te == null || te.getClass() != teClass || te.isInvalid() || te.getWorld() != world)) {


                te = TileEntityBlock.instantiate(teClass);
                world.setTileEntity(pos, te);

                assert !te.isInvalid();

                assert te.getWorld() == world;
            } else {
                if (te == null) {
                    return;
                }


                if (te.isInvalid() || te.getWorld() != world) {
                    return;
                }


            }
            final CustomPacketBuffer buf = new CustomPacketBuffer();
            buf.writeBytes(is);
            ((TileEntityBlock) te).readPacket(buf);

        }
    }

    @Override
    public byte getId() {
        return 0;
    }

    @Override
    public void readPacket(final CustomPacketBuffer is, final EntityPlayer entityPlayer) {
        final int dimensionId = is.readInt();
        BlockPos pos;
        try {
            pos = DecoderHandler.decode(is, BlockPos.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String fieldName = is.readString();
        String[] parts = fieldName.split("=");
        String firstPart = parts[0];
        String secondPart = parts[1];

        String[] subParts = firstPart.split(":");
        String prefix = subParts[0];
        String identifier = subParts[1];
        final ResourceLocation res = new ResourceLocation(prefix, identifier);
        Class<? extends TileEntityBlock> teClass = TileBlockCreator.instance.get(res).teInfo.getClassFromName(secondPart);

        byte[] bytes = new byte[is.writerIndex() - is.readerIndex()];
        is.readBytes(bytes);
        if (!(is.readerIndex() < is.writerIndex())) {
            IUCore.proxy.requestTick(false, () -> {
                World world = IUCore.proxy.getPlayerWorld();
                if (world != null && world.provider.getDimension() == dimensionId) {
                    apply(pos, teClass, world, bytes);

                }
            });
        }
    }

    @Override
    public EnumTypePacket getPacketType() {
        return EnumTypePacket.SERVER;
    }

}
