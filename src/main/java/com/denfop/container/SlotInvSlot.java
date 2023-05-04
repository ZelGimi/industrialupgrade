package com.denfop.container;

import com.denfop.invslot.InvSlot;
import ic2.core.util.StackUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotInvSlot extends Slot {

    public final InvSlot invSlot;
    public final int index;

    public SlotInvSlot(InvSlot invSlot, int index, int x, int y) {
        super(invSlot.base.getParent(), invSlot.base.getBaseIndex(invSlot) + index, x, y);
        this.invSlot = invSlot;
        this.index = index;
    }

    public boolean isItemValid(ItemStack stack) {
        return this.invSlot.accepts(stack, this.index);
    }

    public ItemStack getStack() {
        return this.invSlot.get(this.index);
    }

    public void putStack(ItemStack stack) {
        this.invSlot.put(this.index, stack);
        this.onSlotChanged();
    }

    public ItemStack decrStackSize(int amount) {
        if (amount <= 0) {
            return StackUtil.emptyStack;
        } else {
            ItemStack stack = this.invSlot.get(this.index);
            if (StackUtil.isEmpty(stack)) {
                return StackUtil.emptyStack;
            } else {
                amount = Math.min(amount, StackUtil.getSize(stack));
                ItemStack ret;
                if (StackUtil.getSize(stack) == amount) {
                    ret = stack;
                    this.invSlot.clear(this.index);
                } else {
                    ret = StackUtil.copyWithSize(stack, amount);
                    this.invSlot.put(this.index, StackUtil.decSize(stack, amount));
                }

                this.onSlotChanged();
                return ret;
            }
        }
    }

    public boolean isHere(IInventory inventory, int index) {
        if (inventory != this.invSlot.base) {
            return false;
        } else {
            int baseIndex = this.invSlot.base.getBaseIndex(this.invSlot);
            if (baseIndex == -1) {
                return false;
            } else {
                return baseIndex + this.index == index;
            }
        }
    }

    public int getSlotStackLimit() {
        return this.invSlot.getStackSizeLimit();
    }

    public ItemStack onTake(EntityPlayer player, ItemStack stack) {
        stack = super.onTake(player, stack);
        this.invSlot.onPickupFromSlot(player, stack);
        return stack;
    }

}
