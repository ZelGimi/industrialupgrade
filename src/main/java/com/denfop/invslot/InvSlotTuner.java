package com.denfop.invslot;

import com.denfop.items.modules.ItemAdditionModule;
import com.denfop.tiles.base.TileEntityInventory;
import ic2.core.block.invslot.InvSlot;
import net.minecraft.item.ItemStack;

public class InvSlotTuner extends InvSlot {

    private int stackSizeLimit;

    public InvSlotTuner(TileEntityInventory base1, String name) {
        super(base1, name, InvSlot.Access.IO, 1, InvSlot.InvSide.ANY);
        this.stackSizeLimit = 1;
    }

    public boolean accepts(ItemStack itemStack) {

        return itemStack.getItem() instanceof ItemAdditionModule && itemStack.getItemDamage() == 10;

    }

    public int getStackSizeLimit() {
        return this.stackSizeLimit;
    }

    public void setStackSizeLimit(int stackSizeLimit) {
        this.stackSizeLimit = stackSizeLimit;
    }

}
