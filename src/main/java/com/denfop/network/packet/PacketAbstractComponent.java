package com.denfop.network.packet;

import com.denfop.IUCore;
import com.denfop.blockentity.base.BlockEntityBase;
import com.denfop.componets.AbstractComponent;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.io.IOException;

public class PacketAbstractComponent implements IPacket {


    private CustomPacketBuffer buffer;

    public PacketAbstractComponent() {
    }

    public PacketAbstractComponent(
            BlockEntityBase te,
            String componentName,
            ServerPlayer player,
            CustomPacketBuffer data
    ) {
        CustomPacketBuffer buffer = new CustomPacketBuffer(64, player.registryAccess());
        try {
            buffer.writeByte(this.getId());
            EncoderHandler.encode(buffer, te.getBlockPos(), false);
            buffer.writeString(componentName);
            buffer.writeBytes(data);
        } catch (IOException var7) {
            throw new RuntimeException(var7);
        }
        this.buffer = buffer;
        IUCore.network.getServer().sendPacket(this, player);
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
        return 2;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void readPacket(final CustomPacketBuffer is, final Player entityPlayer) {
        final BlockPos pos1;
        try {
            pos1 = DecoderHandler.decode(is, BlockPos.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String componentName = is.readString();

        final byte[] data = new byte[is.writerIndex() - is.readerIndex()];
        is.readBytes(data);
        BlockEntity teRaw = entityPlayer.level().getBlockEntity(pos1);
        if (teRaw instanceof BlockEntityBase) {
            BlockEntityBase tile = (BlockEntityBase) teRaw;
            AbstractComponent component = tile.getComp(componentName);

            if (component != null) {
                try {

                    component.onNetworkUpdate(new CustomPacketBuffer(data, is.registryAccess()));
                } catch (IOException var6) {
                    throw new RuntimeException(var6);
                }
            }
        }
    }


    @Override
    public EnumTypePacket getPacketType() {
        return EnumTypePacket.SERVER;
    }


}
