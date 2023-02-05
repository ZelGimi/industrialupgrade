package com.denfop.invslot;

import com.denfop.tiles.base.TileEntityInventory;
import ic2.core.block.invslot.InvSlot;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemStack;

public class InvSlotFisher extends InvSlot {

    private int stackSizeLimit;

    public InvSlotFisher(TileEntityInventory base1) {
        super(base1, "input2", InvSlot.Access.I, 1, InvSlot.InvSide.ANY);
        this.stackSizeLimit = 1;
    }


    public boolean accepts(ItemStack itemStack) {
        return itemStack.getItem() instanceof ItemFishingRod;
    }

    public int getStackSizeLimit() {
        return this.stackSizeLimit;
    }

    public void setStackSizeLimit(int stackSizeLimit) {
        this.stackSizeLimit = stackSizeLimit;
    }

    public void consume(int amount) {
        consume(amount, false);
    }

    public void consume(int amount, boolean simulate) {
        for (int i = 0; i < size(); i++) {
            ItemStack stack = get(i);
            if (!stack.isEmpty() && amount > 0) {
                int currentAmount = Math.min(amount, stack.getCount());
                if (!simulate) {
                    stack.shrink(currentAmount);
                }
                amount -= currentAmount;
                if (amount == 0) {
                    break;
                }
            }
        }
    }

}
