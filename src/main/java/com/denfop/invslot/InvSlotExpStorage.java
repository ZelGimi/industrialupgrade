package com.denfop.invslot;

import com.denfop.IUItem;
import ic2.core.block.TileEntityInventory;
import ic2.core.block.invslot.InvSlot;
import net.minecraft.item.ItemStack;

public class InvSlotExpStorage extends InvSlot {

    private int stackSizeLimit;

    public InvSlotExpStorage(TileEntityInventory base1) {
        super(base1, "input5", InvSlot.Access.I, 1, InvSlot.InvSide.TOP);

        this.stackSizeLimit = 1;
    }

    public boolean accepts(ItemStack itemStack) {

        return itemStack.getItem().equals(IUItem.expmodule);
    }

    public int getStackSizeLimit() {
        return this.stackSizeLimit;
    }

    public void setStackSizeLimit(int stackSizeLimit) {
        this.stackSizeLimit = stackSizeLimit;
    }

}
