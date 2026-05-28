package com.denfop.network.packet;


import com.denfop.IUCore;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.io.IOException;

public class PackerUpdateClientRemoveStack implements IPacket {

    public PackerUpdateClientRemoveStack() {

    }


    public PackerUpdateClientRemoveStack(Player player, ItemStack stack) {
        CustomPacketBuffer customPacketBuffer = new CustomPacketBuffer();
        customPacketBuffer.writeByte(getId());

        try {
            EncoderHandler.encode(customPacketBuffer, stack);
        } catch (IOException ignored) {
        }
        IUCore.network.getServer().sendPacket(customPacketBuffer, (ServerPlayer) player);
    }

    @Override
    public byte getId() {
        return 113;
    }

    @Override
    public void readPacket(final CustomPacketBuffer customPacketBuffer, final Player entityPlayer) {
        try {
            ItemStack stack = (ItemStack) DecoderHandler.decode(customPacketBuffer);
            entityPlayer.containerMenu.setCarried(stack);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public EnumTypePacket getPacketType() {
        return EnumTypePacket.SERVER;
    }
}

