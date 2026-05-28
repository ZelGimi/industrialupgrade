package com.denfop.network.packet;


import com.denfop.IUCore;
import com.denfop.blockentity.storage.BlockEntityMonitor;
import com.denfop.blockentity.storage.BlockEntityPatternMonitor;
import com.denfop.network.DecoderHandler;
import com.denfop.network.EncoderHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.io.IOException;
import java.util.UUID;

public class PacketRemoveShiftStack implements IPacket {

    private CustomPacketBuffer buffer;

    public PacketRemoveShiftStack() {

    }


    public PacketRemoveShiftStack(Player player, ItemStack stack, Level level, BlockPos pos) {
        CustomPacketBuffer customPacketBuffer = new CustomPacketBuffer(level.registryAccess());
        customPacketBuffer.writeByte(getId());

        try {
            EncoderHandler.encode(customPacketBuffer, pos);
            EncoderHandler.encode(customPacketBuffer, level);
            EncoderHandler.encode(customPacketBuffer, player.getUUID());
            EncoderHandler.encode(customPacketBuffer, stack);
        } catch (IOException e) {
            throw new RuntimeException("Failed to encode PacketRemoveShiftStack", e);
        }
        this.buffer = customPacketBuffer;
        IUCore.network.getClient().sendPacket(this, customPacketBuffer);
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
        return 116;
    }

    @Override
    public void readPacket(final CustomPacketBuffer customPacketBuffer, final Player entityPlayer) {
        try {
            BlockPos pos = (BlockPos) DecoderHandler.decode(customPacketBuffer);
            Level level = (Level) DecoderHandler.decode(customPacketBuffer);
            UUID uuid = (UUID) DecoderHandler.decode(customPacketBuffer);
            if (entityPlayer.getUUID().equals(uuid))
                if (entityPlayer.level().getBlockEntity(pos) instanceof BlockEntityMonitor controller && controller.network != null) {
                    ItemStack stack = (ItemStack) DecoderHandler.decode(customPacketBuffer);

                    int count = controller.network.canRemoveMonitor(stack);
                    stack.setCount(count);
                    ItemStack stack1 = stack.copy();
                    ItemStack stack2 = addItemStackToInventory(entityPlayer, stack1);
                    if (stack.getCount() - stack2.getCount() != 0) {
                        stack.setCount(stack.getCount() - stack2.getCount());
                        controller.network.removeStack(stack);
                    }
                }
            if (entityPlayer.level().getBlockEntity(pos) instanceof BlockEntityPatternMonitor controller && controller.network != null) {
                ItemStack stack = (ItemStack) DecoderHandler.decode(customPacketBuffer);

                int count = controller.network.canRemoveMonitor(stack);
                stack.setCount(count);
                ItemStack stack1 = stack.copy();
                ItemStack stack2 = addItemStackToInventory(entityPlayer, stack1);
                if (stack.getCount() - stack2.getCount() != 0) {
                    stack.setCount(stack.getCount() - stack2.getCount());
                    controller.network.removeStack(stack);
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public ItemStack addItemStackToInventory(Player player, ItemStack stack) {
        if (stack.isEmpty()) return ItemStack.EMPTY;

        Inventory inv = player.getInventory();
        for (int i = 0; i < inv.items.size(); i++) {
            ItemStack slot = inv.items.get(i);
            if (!slot.isEmpty() && ItemStack.isSameItemSameComponents(stack, slot)) {
                int transferable = Math.min(stack.getCount(), slot.getMaxStackSize() - slot.getCount());
                if (transferable > 0) {
                    slot.grow(transferable);
                    stack.shrink(transferable);
                    if (stack.isEmpty()) return ItemStack.EMPTY;
                }
            }
        }

        for (int i = 0; i < inv.items.size(); i++) {
            ItemStack slot = inv.items.get(i);
            if (slot.isEmpty()) {
                int toPut = Math.min(stack.getCount(), stack.getMaxStackSize());
                inv.items.set(i, stack.split(toPut));
                if (stack.isEmpty()) return ItemStack.EMPTY;
            }
        }


        return stack;
    }

    @Override
    public EnumTypePacket getPacketType() {
        return EnumTypePacket.CLIENT;
    }
}

