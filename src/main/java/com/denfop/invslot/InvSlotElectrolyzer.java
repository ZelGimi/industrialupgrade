package com.denfop.invslot;

import com.denfop.IUItem;
import com.denfop.tiles.base.TileEntityInventory;
import net.minecraft.item.ItemStack;

public class InvSlotElectrolyzer extends InvSlot {

    private final int type;
    private int stackSizeLimit;

    public InvSlotElectrolyzer(TileEntityInventory base1, int type) {
        super(base1, TypeItemSlot.INPUT, 1);
        this.type = type;
        this.stackSizeLimit = 1;
    }

    public static boolean isStackEqual(ItemStack stack1, ItemStack stack2) {
        return stack1 == null && stack2 == null || stack1 != null && stack2 != null && stack1.getItem() == stack2.getItem() && (!stack1.getHasSubtypes() && !stack1.isItemStackDamageable() || stack1.getItemDamage() == stack2.getItemDamage());
    }

    public static boolean isStackEqualStrict(ItemStack stack1, ItemStack stack2) {
        return isStackEqual(stack1, stack2) && ItemStack.areItemStackTagsEqual(stack1, stack2);
    }

    public boolean accepts(ItemStack itemStack, final int index) {
        if (type == 0) {
            return itemStack.getItem().equals(IUItem.anode);
        }
        if (type == 1) {
            return itemStack.getItem().equals(IUItem.cathode);
        }
        return false;
    }

    public int getStackSizeLimit() {
        return this.stackSizeLimit;
    }

    public void setStackSizeLimit(int stackSizeLimit) {
        this.stackSizeLimit = stackSizeLimit;
    }

    public void consume(int amount) {
        this.contents.get(0).shrink(amount);
    }


}
