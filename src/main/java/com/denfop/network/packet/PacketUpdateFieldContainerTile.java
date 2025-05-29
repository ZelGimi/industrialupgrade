package com.denfop.network.packet;

import com.denfop.IUCore;
import com.denfop.network.DecoderHandler;
import com.denfop.tiles.base.TileEntityBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.io.IOException;

public class PacketUpdateFieldContainerTile implements IPacket {
    public PacketUpdateFieldContainerTile() {

    }

    public PacketUpdateFieldContainerTile(TileEntityBlock te, ServerPlayer player) {
        IUCore.network.getServer().addTileContainerToUpdate(te, player, te.writeContainerPacket());
    }

    public PacketUpdateFieldContainerTile(CustomPacketBuffer customPacketBuffer, ServerPlayer entityPlayer) {

        IUCore.network.getServer().sendPacket(customPacketBuffer, entityPlayer);
    }

    public static void apply(BlockPos pos, Level world, byte[] is) {
        if (world.isLoaded(pos)) {
            BlockEntity te = world.getBlockEntity(pos);
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
    public void readPacket(final CustomPacketBuffer is, final Player entityPlayer) {
        BlockPos pos;
        try {
            pos = DecoderHandler.decode(is, BlockPos.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        byte[] bytes = new byte[is.writerIndex() - is.readerIndex()];
        is.readBytes(bytes);
        if (!(is.readerIndex() < is.writerIndex())) {
            Level world = entityPlayer.level();
            apply(pos, world, bytes);
        }
    }

    @Override
    public EnumTypePacket getPacketType() {
        return EnumTypePacket.SERVER;
    }
}
