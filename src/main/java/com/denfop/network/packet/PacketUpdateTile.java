package com.denfop.network.packet;

import com.denfop.IUCore;
import com.denfop.api.tile.IMultiTileBlock;
import com.denfop.blocks.TileBlockCreator;
import com.denfop.network.DecoderHandler;
import com.denfop.tiles.base.TileEntityBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.io.IOException;

public class PacketUpdateTile implements IPacket {

    public PacketUpdateTile() {

    }

    public PacketUpdateTile(TileEntityBlock te) {

        IUCore.network.getServer().addTileToUpdate(te);

    }

    public PacketUpdateTile(CustomPacketBuffer data, ServerPlayer player) {

        IUCore.network.getServer().sendPacket(data, player);
    }

    private static void apply(BlockPos pos, Class<? extends TileEntityBlock> teClass, Level world, byte[] is) {
        if (world.isLoaded(pos)) {
            BlockEntity te = world.getBlockEntity(pos);
            if (teClass != null && (te == null || te.getClass() != teClass || te.isRemoved() || te.getLevel() != world)) {

                System.out.println(2);
            } else {
                if (te == null) {
                    return;
                }


                if (te.isRemoved() || te.getLevel() != world) {
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
    public void readPacket(final CustomPacketBuffer is, final Player entityPlayer) {
        BlockPos pos;
        try {
            pos = DecoderHandler.decode(is, BlockPos.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        int firstPart = is.readShort();
        Class<? extends TileEntityBlock> teClass = ((IMultiTileBlock) TileBlockCreator.instance.get(firstPart).teInfo.getListBlock().get(0)).getTeClass();
        byte[] bytes = new byte[is.writerIndex() - is.readerIndex()];
        is.readBytes(bytes);
        if (!(is.readerIndex() < is.writerIndex())) {
            apply(pos, teClass, entityPlayer.getLevel(), bytes);
        }
    }

    @Override
    public EnumTypePacket getPacketType() {
        return EnumTypePacket.SERVER;
    }

}
