package com.denfop.network.packet;

import com.denfop.IUCore;
import com.denfop.network.EncoderHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import java.io.IOException;

public class PacketItemStackUpdate implements IPacket {

    public PacketItemStackUpdate() {

    }

    public PacketItemStackUpdate(String name, Object o, EntityPlayerMP playerMP) {
        CustomPacketBuffer buffer = new CustomPacketBuffer();
        buffer.writeByte(this.getId());
        buffer.writeString(name);
        try {
            EncoderHandler.encode(buffer, o);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        IUCore.network.getServer().sendPacket(buffer, playerMP);
    }

    @Override
    public byte getId() {
        return 30;
    }

    @Override
    public void readPacket(final CustomPacketBuffer is, final EntityPlayer entityPlayer) {
        if (!entityPlayer.getHeldItem(EnumHand.MAIN_HAND).isEmpty()) {
            ItemStack stack = entityPlayer.getHeldItem(EnumHand.MAIN_HAND);
            if (stack.getItem() instanceof IUpdatableItemStackEvent) {
                byte[] bytes = new byte[is.writerIndex() - is.readerIndex()];
                is.readBytes(bytes);
                if (!(is.readerIndex() < is.writerIndex())) {
                    IUCore.proxy.requestTick(false, () -> {
                        World world = IUCore.proxy.getPlayerWorld();
                        if (world != null && world.provider.getDimension() == entityPlayer.getEntityWorld().provider.getDimension()) {
                            apply(stack, bytes);

                        }
                    });
                }
            }
        }
    }

    private void apply(ItemStack stack, byte[] is) {
        final CustomPacketBuffer buf = new CustomPacketBuffer();
        buf.writeBytes(is);

        ((IUpdatableItemStackEvent) stack.getItem()).updateField(buf.readString(), buf, stack);

    }

    @Override
    public EnumTypePacket getPacketType() {
        return EnumTypePacket.SERVER;
    }

}
