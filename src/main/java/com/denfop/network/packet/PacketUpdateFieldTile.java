package com.denfop.network.packet;

import com.denfop.IUCore;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.tiles.base.TileEntityBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.PacketDistributor;

import java.io.IOException;

public class PacketUpdateFieldTile implements IPacket {

    public PacketUpdateFieldTile() {

    }

    public PacketUpdateFieldTile(TileEntityBlock te, String field, Object o) {
        final CustomPacketBuffer packet = new CustomPacketBuffer();
        packet.writeString(field);
        try {
            EncoderHandler.encode(packet, o);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        IUCore.network.getServer().addTileFieldToUpdate(te, packet);
    }

    public PacketUpdateFieldTile(CustomPacketBuffer customPacketBuffer, ServerPlayer entityPlayer) {
        IUCore.network.getServer().sendPacket(PacketDistributor.PLAYER.with(() -> entityPlayer), customPacketBuffer);
    }

    public static void apply(BlockPos pos, Level world, byte[] is) {
        BlockEntity te = world.getBlockEntity(pos);
        final CustomPacketBuffer buf = new CustomPacketBuffer();
        buf.writeBytes(is);
        if (te != null) {
            ((TileEntityBlock) te).updateField(buf.readString().trim(), buf);
        }
    }

    @Override
    public byte getId() {
        return 12;
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
            Level world = entityPlayer.getLevel();
            if (world != null) {
                apply(pos, world, bytes);

            }
        }

    }

    @Override
    public EnumTypePacket getPacketType() {
        return EnumTypePacket.SERVER;
    }

}
