package com.denfop.container;

import com.denfop.invslot.Inventory;
import com.denfop.utils.ModUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotInvSlot extends Slot {

    public final Inventory inventory;
    public final int index;

    public SlotInvSlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
        this.inventory = inventory;
        this.index = index;
    }

    public int getJeiX() {
        return this.xPos - 1;
    }

    public int getJeiY() {
        return this.yPos - 1;
    }

    public boolean isItemValid(ItemStack stack) {
        return this.inventory.isItemValidForSlot(this.index, stack);
    }

    public ItemStack getStack() {
        return this.inventory.get(this.index);
    }

    public void putStack(ItemStack stack) {
        this.inventory.put(this.index, stack);
        this.onSlotChanged();
    }


    public ItemStack decrStackSize(int amount) {
        if (amount <= 0) {
            return ModUtils.emptyStack;
        } else {
            ItemStack stack = this.inventory.get(this.index);
            if (ModUtils.isEmpty(stack)) {
                return ModUtils.emptyStack;
            } else {
                amount = Math.min(amount, ModUtils.getSize(stack));
                ItemStack ret;
                if (ModUtils.getSize(stack) == amount) {
                    ret = stack;
                    this.inventory.set(this.index,ItemStack.EMPTY);
                } else {
                    ret = ModUtils.setSize(stack, amount);
                    this.inventory.put(this.index, ModUtils.decSize(stack, amount));
                }

                this.onSlotChanged();
                return ret;
            }
        }
    }

    public boolean isHere(IInventory inventory, int index) {
        if (inventory != this.inventory) {
            return false;
        } else {
            return this.index == index;
        }
    }

    public int getSlotStackLimit() {
        return this.inventory.getInventoryStackLimit();
    }

    public ItemStack onTake(EntityPlayer player, ItemStack stack) {
        stack = super.onTake(player, stack);
        return stack;
    }

}
