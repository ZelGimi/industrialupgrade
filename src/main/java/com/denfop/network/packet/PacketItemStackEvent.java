package com.denfop.network.packet;

import com.denfop.IUCore;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class PacketItemStackEvent implements IPacket {

    public PacketItemStackEvent() {

    }

    public PacketItemStackEvent(int event, Player player) {
        CustomPacketBuffer buffer = new CustomPacketBuffer();
        buffer.writeByte(this.getId());
        buffer.writeString(player.getName().getString());
        buffer.writeInt(event);

        IUCore.network.getClient().sendPacket(buffer);
    }

    @Override
    public byte getId() {
        return 31;
    }

    @Override
    public void readPacket(final CustomPacketBuffer customPacketBuffer, final Player entityPlayer) {
        if (!entityPlayer.getName().getString().equals(customPacketBuffer.readString())) {
            return;
        }
        ItemStack stack = entityPlayer.getItemInHand(InteractionHand.MAIN_HAND);
        if (!stack.isEmpty() && stack.getItem() instanceof IUpdatableItemStackEvent) {
            ((IUpdatableItemStackEvent) stack.getItem()).updateEvent(customPacketBuffer.readInt(), stack);
        }
    }

    @Override
    public EnumTypePacket getPacketType() {
        return EnumTypePacket.CLIENT;
    }

}
