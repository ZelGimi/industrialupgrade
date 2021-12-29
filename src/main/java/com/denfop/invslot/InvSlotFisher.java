package com.denfop.invslot;

import ic2.core.block.TileEntityInventory;
import ic2.core.block.invslot.InvSlot;
import ic2.core.util.StackUtil;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemStack;

public class InvSlotFisher extends InvSlot {

    private int stackSizeLimit;

    public InvSlotFisher(TileEntityInventory base1) {
        super(base1, "input2", InvSlot.Access.I, 1, InvSlot.InvSide.TOP);
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

    public static boolean isStackEqual(ItemStack stack1, ItemStack stack2) {
        return stack1 == null && stack2 == null || stack1 != null && stack2 != null && stack1.getItem() == stack2.getItem() && (!stack1.getHasSubtypes() && !stack1.isItemStackDamageable() || stack1.getItemDamage() == stack2.getItemDamage());
    }

    public static boolean isStackEqualStrict(ItemStack stack1, ItemStack stack2) {
        return isStackEqual(stack1, stack2) && ItemStack.areItemStackTagsEqual(stack1, stack2);
    }

    public void consume(int amount) {
        consume(amount, false, false);
    }

    public void consume(int amount, boolean simulate, boolean consumeContainers) {
        ItemStack ret = null;
        for (int i = 0; i < size(); i++) {
            ItemStack stack = get(i);
            if (stack != null && stack.getCount() >= 1 &&

                    accepts(stack) && (ret == null ||
                    isStackEqualStrict(stack, ret)) && (stack.getCount() == 1 || consumeContainers ||
                    !stack.getItem().hasContainerItem(stack))) {
                int currentAmount = Math.min(amount, stack.getCount());
                amount -= currentAmount;
                if (!simulate) {
                    if (stack.getCount() == currentAmount) {
                        if (!consumeContainers && stack.getItem().hasContainerItem(stack)) {
                            put(i, stack.getItem().getContainerItem(stack));
                        } else {
                            put(i, null);
                        }
                    } else {
                        stack.setCount(stack.getCount() - currentAmount);
                    }
                }
                if (ret == null) {
                    ret = StackUtil.copyWithSize(stack, currentAmount);
                } else {
                    ret.setCount(ret.getCount() + currentAmount);
                }
                if (amount == 0) {
                    break;
                }
            }
        }
    }

}
