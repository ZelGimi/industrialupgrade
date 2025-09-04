package com.denfop.network.packet;

import com.denfop.IUCore;
import com.denfop.api.blockentity.MultiBlockEntity;
import com.denfop.blockentity.base.BlockEntityBase;
import com.denfop.blocks.TileBlockCreator;
import com.denfop.network.DecoderHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.io.IOException;

public class PacketUpdateTile implements IPacket {

    private CustomPacketBuffer buffer;

    public PacketUpdateTile() {

    }

    public PacketUpdateTile(BlockEntityBase te) {

        IUCore.network.getServer().addTileToUpdate(te);

    }

    public PacketUpdateTile(CustomPacketBuffer data, ServerPlayer player) {

        this.buffer = data;
        IUCore.network.getServer().sendPacket(this, data, player);
    }

    private static void apply(BlockPos pos, Class<? extends BlockEntityBase> teClass, Level world, byte[] is) {
        if (world.isLoaded(pos)) {
            BlockEntity te = world.getBlockEntity(pos);
            if (teClass != null && (te == null || te.getClass() != teClass || te.isRemoved() || te.getLevel() != world)) {


            } else {
                if (te == null) {
                    return;
                }


                if (te.isRemoved() || te.getLevel() != world) {
                    return;
                }


            }
            final CustomPacketBuffer buf = new CustomPacketBuffer(world.registryAccess());
            buf.writeBytes(is);
            ((BlockEntityBase) te).readPacket(buf);

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
        Class<? extends BlockEntityBase> teClass = ((MultiBlockEntity) TileBlockCreator.instance.get(firstPart).teInfo.getListBlock().get(0)).getTeClass();
        byte[] bytes = new byte[is.writerIndex() - is.readerIndex()];
        is.readBytes(bytes);
        if (!(is.readerIndex() < is.writerIndex())) {
            apply(pos, teClass, entityPlayer.level(), bytes);
        }
    }

    @Override
    public EnumTypePacket getPacketType() {
        return EnumTypePacket.SERVER;
    }

}
