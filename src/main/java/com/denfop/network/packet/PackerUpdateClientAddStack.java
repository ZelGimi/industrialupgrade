package com.denfop.network.packet;


import com.denfop.IUCore;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.io.IOException;

public class PackerUpdateClientAddStack implements IPacket {

    private CustomPacketBuffer buffer;

    public PackerUpdateClientAddStack() {

    }


    public PackerUpdateClientAddStack(Player player, ItemStack stack) {
        CustomPacketBuffer customPacketBuffer = new CustomPacketBuffer(player.registryAccess());
        customPacketBuffer.writeByte(getId());

        try {
            EncoderHandler.encode(customPacketBuffer, stack);
        } catch (IOException ignored) {
        }
        this.buffer = customPacketBuffer;
        IUCore.network.getServer().sendPacket(this, customPacketBuffer, (ServerPlayer) player);
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
        return 115;
    }

    @Override
    public void readPacket(final CustomPacketBuffer customPacketBuffer, final Player entityPlayer) {
        try {
            ItemStack stack = (ItemStack) DecoderHandler.decode(customPacketBuffer);
            int count1 = entityPlayer.containerMenu.getCarried().getCount() - stack.getCount();
            entityPlayer.containerMenu.setCarried(entityPlayer.containerMenu.getCarried().split(count1));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public EnumTypePacket getPacketType() {
        return EnumTypePacket.SERVER;
    }
}

