package com.denfop.invslot;

import com.denfop.items.modules.ItemAdditionModule;
import com.denfop.tiles.base.TileEntityInventory;
import net.minecraft.item.ItemStack;

public class InvSlotTuner extends InvSlot {

    private int stackSizeLimit;

    public InvSlotTuner(TileEntityInventory base1) {
        super(base1, TypeItemSlot.INPUT_OUTPUT, 1);
        this.stackSizeLimit = 1;
    }

    public boolean accepts(ItemStack itemStack, final int index) {

        return itemStack.getItem() instanceof ItemAdditionModule && itemStack.getItemDamage() == 10;

    }

    public int getStackSizeLimit() {
        return this.stackSizeLimit;
    }

    public void setStackSizeLimit(int stackSizeLimit) {
        this.stackSizeLimit = stackSizeLimit;
    }

}
