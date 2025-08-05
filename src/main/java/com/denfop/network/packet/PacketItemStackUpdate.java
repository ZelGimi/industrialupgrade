package com.denfop.network.packet;

import com.denfop.IUCore;
import com.denfop.network.EncoderHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.io.IOException;

public class PacketItemStackUpdate implements IPacket {

    private CustomPacketBuffer buffer;

    public PacketItemStackUpdate() {

    }

    public PacketItemStackUpdate(String name, Object o, ServerPlayer playerMP) {
        CustomPacketBuffer buffer = new CustomPacketBuffer(playerMP.registryAccess());
        buffer.writeByte(this.getId());
        buffer.writeString(name);
        try {
            EncoderHandler.encode(buffer, o);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.buffer = buffer;
        IUCore.network.getServer().sendPacket(this, buffer, playerMP);
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
        return 30;
    }

    @Override
    public void readPacket(final CustomPacketBuffer is, final Player entityPlayer) {
        if (!entityPlayer.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
            ItemStack stack = entityPlayer.getItemInHand(InteractionHand.MAIN_HAND);
            if (stack.getItem() instanceof IUpdatableItemStackEvent) {
                byte[] bytes = new byte[is.writerIndex() - is.readerIndex()];
                is.readBytes(bytes);
                if (!(is.readerIndex() < is.writerIndex())) {

                    Level world = entityPlayer.level();
                    apply(stack, bytes, world);
                }
            }
        }
    }


    private void apply(ItemStack stack, byte[] is, Level world) {
        final CustomPacketBuffer buf = new CustomPacketBuffer(world.registryAccess());
        buf.writeBytes(is);

        ((IUpdatableItemStackEvent) stack.getItem()).updateField(buf.readString(), buf, stack);

    }

    @Override
    public EnumTypePacket getPacketType() {
        return EnumTypePacket.SERVER;
    }

}
