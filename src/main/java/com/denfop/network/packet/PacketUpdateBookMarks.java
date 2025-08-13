package com.denfop.network.packet;

import com.denfop.IUCore;
import com.denfop.datacomponent.DataComponentsInit;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import com.denfop.utils.ModUtils;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

public class PacketUpdateBookMarks implements IPacket {

    private CustomPacketBuffer buffer;

    public PacketUpdateBookMarks() {

    }

    public PacketUpdateBookMarks(DataComponentPatch event, Player player) {
        CustomPacketBuffer buffer = new CustomPacketBuffer(player.registryAccess());
        buffer.writeByte(this.getId());
        buffer.writeString(player.getName().getString());
        try {
            EncoderHandler.encode(buffer, event);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.buffer = buffer;
        IUCore.network.getClient().sendPacket(this, buffer);
    }
    public CustomPacketBuffer getPacketBuffer() {
        return buffer;
    }

    @Override
    public void setPacketBuffer(CustomPacketBuffer customPacketBuffer) {
        buffer = customPacketBuffer;
    }
    @Override
    public byte getId() {
        return 74;
    }
    @SuppressWarnings("unchecked")
    private <T> void setData(ItemStack stack, DataComponentType<T> key,  Optional<?> value) {
        stack.set(key, (T) value.get());
    }
    @Override
    public void readPacket(final CustomPacketBuffer customPacketBuffer, final Player entityPlayer) {
        if (!entityPlayer.getName().getString().equals(customPacketBuffer.readString())) {
            return;
        }
        ItemStack stack = entityPlayer.getItemInHand(InteractionHand.MAIN_HAND);
        try {
            DataComponentPatch tag = (DataComponentPatch) DecoderHandler.decode(customPacketBuffer);
            for (Map.Entry<DataComponentType<?>, Optional<?>> entry : tag.entrySet()) {
                if (entry.getKey() == DataComponentsInit.BOOKMARK)
                setData(stack, entry.getKey(), entry.getValue());
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public EnumTypePacket getPacketType() {
        return EnumTypePacket.CLIENT;
    }

}
