package com.denfop.blockentity.adv_cokeoven;

import com.denfop.inventory.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;


public class InventoryCokeOven extends Inventory {


    private int stackSizeLimit;

    public InventoryCokeOven(BlockEntityAdvCokeOvenMain base1) {
        super(base1, TypeItemSlot.INPUT, 1);
        this.setStackSizeLimit(64);
    }


    public boolean canPlaceItem(final int index, ItemStack itemStack) {

        return itemStack.getItem().equals(Items.COAL);
    }

    public int getStackSizeLimit() {
        return this.stackSizeLimit;
    }

    public void setStackSizeLimit(int stackSizeLimit) {
        this.stackSizeLimit = stackSizeLimit;
    }


}
