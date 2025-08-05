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

public class PacketUpdateOvertimeTile implements IPacket {

    private CustomPacketBuffer buffer;

    public PacketUpdateOvertimeTile() {

    }

    public PacketUpdateOvertimeTile(CustomPacketBuffer data, ServerPlayer player) {
        this.buffer = data;
        IUCore.network.getServer().sendPacket(this, data, player);
    }

    public static void apply(BlockPos pos, Level world, byte[] is) {
        if (world.isLoaded(pos)) {
            BlockEntity te = world.getBlockEntity(pos);
            final CustomPacketBuffer buf = new CustomPacketBuffer(world.registryAccess());
            buf.writeBytes(is);
            if (te != null) {
                ((TileEntityBlock) te).readUpdatePacket(buf);
            }

        }
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
        return 25;
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
        apply(pos, entityPlayer.level(), bytes);

    }

    @Override
    public EnumTypePacket getPacketType() {
        return EnumTypePacket.SERVER;
    }

}
