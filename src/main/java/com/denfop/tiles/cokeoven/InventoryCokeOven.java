package com.denfop.tiles.cokeoven;

import com.denfop.invslot.Inventory;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;


public class InventoryCokeOven extends Inventory {


    private int stackSizeLimit;

    public InventoryCokeOven(TileCokeOvenMain base1) {
        super(base1, TypeItemSlot.INPUT, 1);
        this.setInventoryStackLimit(64);
    }


    public boolean isItemValidForSlot(final int index, ItemStack itemStack) {

        return itemStack.getItem().equals(Items.COAL);
    }

    public int getInventoryStackLimit() {
        return this.stackSizeLimit;
    }

    public void setInventoryStackLimit(int stackSizeLimit) {
        this.stackSizeLimit = stackSizeLimit;
    }


}
