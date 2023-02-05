package com.denfop.invslot;

import com.denfop.IUItem;
import com.denfop.tiles.mechanism.exp.TileEntityStorageExp;
import ic2.core.block.invslot.InvSlot;
import net.minecraft.item.ItemStack;

public class InvSlotExpStorage extends InvSlot {

    private final TileEntityStorageExp base1;
    private int stackSizeLimit;

    public InvSlotExpStorage(TileEntityStorageExp base1) {
        super(base1, "input5", InvSlot.Access.I, 1, InvSlot.InvSide.ANY);

        this.stackSizeLimit = 1;
        this.base1 = base1;
    }

    @Override
    public void put(final int index, final ItemStack content) {
        super.put(index, content);
        if (this.isEmpty()) {
            this.base1.energy.setCapacity(2000000000);
            if (this.base1.energy.getEnergy() > 2000000000) {
                this.base1.energy.storage = 2000000000;
            }
        }
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
