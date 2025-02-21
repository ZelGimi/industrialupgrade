package com.denfop.container;

import com.denfop.invslot.InvSlot;
import com.denfop.utils.ModUtils;
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

    public int getJeiX() {
        return this.xPos - 1;
    }

    public int getJeiY() {
        return this.yPos - 1;
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
            return ModUtils.emptyStack;
        } else {
            ItemStack stack = this.invSlot.get(this.index);
            if (ModUtils.isEmpty(stack)) {
                return ModUtils.emptyStack;
            } else {
                amount = Math.min(amount, ModUtils.getSize(stack));
                ItemStack ret;
                if (ModUtils.getSize(stack) == amount) {
                    ret = stack;
                    this.invSlot.clear(this.index);
                } else {
                    ret = ModUtils.setSize(stack, amount);
                    this.invSlot.put(this.index, ModUtils.decSize(stack, amount));
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
        return stack;
    }

}
