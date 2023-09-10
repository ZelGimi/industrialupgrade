package com.denfop.network.packet;

import com.denfop.IUCore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

public class PacketItemStackEvent implements IPacket {

    public PacketItemStackEvent() {

    }

    public PacketItemStackEvent(int event, EntityPlayer player) {
        CustomPacketBuffer buffer = new CustomPacketBuffer();
        buffer.writeByte(this.getId());
        buffer.writeString(player.getName());
        buffer.writeInt(event);

        IUCore.network.getClient().sendPacket(buffer);
    }

    @Override
    public byte getId() {
        return 31;
    }

    @Override
    public void readPacket(final CustomPacketBuffer customPacketBuffer, final EntityPlayer entityPlayer) {
        if (!entityPlayer.getName().equals(customPacketBuffer.readString())) {
            return;
        }
        ItemStack stack = entityPlayer.getHeldItem(EnumHand.MAIN_HAND);
        if (!stack.isEmpty() && stack.getItem() instanceof IUpdatableItemStackEvent) {
            ((IUpdatableItemStackEvent) stack.getItem()).updateEvent(customPacketBuffer.readInt(), stack);
        }
    }

    @Override
    public EnumTypePacket getPacketType() {
        return EnumTypePacket.CLIENT;
    }

}
