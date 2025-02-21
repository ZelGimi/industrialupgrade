package com.denfop.tiles.cokeoven;

import com.denfop.invslot.InvSlot;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;


public class InvSlotCokeOven extends InvSlot {


    private int stackSizeLimit;

    public InvSlotCokeOven(TileCokeOvenMain base1) {
        super(base1, TypeItemSlot.INPUT, 1);
        this.setStackSizeLimit(64);
    }


    public boolean accepts(ItemStack itemStack, final int index) {

        return itemStack.getItem().equals(Items.COAL);
    }

    public int getStackSizeLimit() {
        return this.stackSizeLimit;
    }

    public void setStackSizeLimit(int stackSizeLimit) {
        this.stackSizeLimit = stackSizeLimit;
    }


}
