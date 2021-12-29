package com.denfop.invslot;


import com.denfop.items.modules.SpawnerModules;
import ic2.core.block.TileEntityInventory;
import ic2.core.block.invslot.InvSlot;
import net.minecraft.item.ItemStack;

public class InvSlotUpgradeModule extends InvSlot {

    private int stackSizeLimit;

    public InvSlotUpgradeModule(TileEntityInventory base1) {
        super(base1, "UpgradeModule", InvSlot.Access.I, 4, InvSlot.InvSide.TOP);

        this.stackSizeLimit = 1;
    }

    public boolean canOutput() {
        return false;
    }

    public boolean accepts(ItemStack itemStack) {

        return itemStack.getItem() instanceof SpawnerModules;
    }

    public int getStackSizeLimit() {
        return this.stackSizeLimit;
    }

    public void setStackSizeLimit(int stackSizeLimit) {
        this.stackSizeLimit = stackSizeLimit;
    }

}
