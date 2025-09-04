package com.denfop.network.packet;

import com.denfop.IUCore;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.io.IOException;

public class PacketUpdateBookMarks implements IPacket {

    public PacketUpdateBookMarks() {

    }

    public PacketUpdateBookMarks(CompoundTag event, Player player) {
        CustomPacketBuffer buffer = new CustomPacketBuffer();
        buffer.writeByte(this.getId());
        buffer.writeString(player.getName().getString());
        try {
            EncoderHandler.encode(buffer, event);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        IUCore.network.getClient().sendPacket(buffer);
    }

    @Override
    public byte getId() {
        return 74;
    }

    @Override
    public void readPacket(final CustomPacketBuffer customPacketBuffer, final Player entityPlayer) {
        if (!entityPlayer.getName().getString().equals(customPacketBuffer.readString())) {
            return;
        }
        ItemStack stack = entityPlayer.getItemInHand(InteractionHand.MAIN_HAND);
        try {
            CompoundTag tag = (CompoundTag) DecoderHandler.decode(customPacketBuffer);
            stack.setTag(tag);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public EnumTypePacket getPacketType() {
        return EnumTypePacket.CLIENT;
    }

}
