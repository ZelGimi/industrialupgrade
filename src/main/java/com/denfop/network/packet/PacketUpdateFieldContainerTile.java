package com.denfop.network.packet;

import com.denfop.IUCore;
import com.denfop.network.DecoderHandler;
import com.denfop.tiles.base.TileEntityBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.io.IOException;

public class PacketUpdateFieldContainerTile implements IPacket {

    public PacketUpdateFieldContainerTile() {

    }

    public PacketUpdateFieldContainerTile(TileEntityBlock te, EntityPlayerMP player) {
        IUCore.network.getServer().addTileContainerToUpdate(te, player, te.writeContainerPacket());
    }

    public PacketUpdateFieldContainerTile(CustomPacketBuffer customPacketBuffer, EntityPlayerMP entityPlayer) {

        IUCore.network.getServer().sendPacket(customPacketBuffer, entityPlayer);
    }

    public static void apply(BlockPos pos, World world, byte[] is) {
        if (world.isBlockLoaded(pos, false)) {
            TileEntity te = world.getTileEntity(pos);
            final CustomPacketBuffer buf = new CustomPacketBuffer();
            buf.writeBytes(is);
            if (te != null) {
                ((TileEntityBlock) te).readContainerPacket(buf);
            }

        }
    }

    @Override
    public byte getId() {
        return 8;
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
        byte[] bytes = new byte[is.writerIndex() - is.readerIndex()];
        is.readBytes(bytes);
        if (!(is.readerIndex() < is.writerIndex())) {
            IUCore.proxy.requestTick(false, () -> {
                World world = IUCore.proxy.getPlayerWorld();
                if (world != null && world.provider.getDimension() == dimensionId) {
                    apply(pos, world, bytes);

                }
            });
        }
    }

    @Override
    public EnumTypePacket getPacketType() {
        return EnumTypePacket.SERVER;
    }

}
