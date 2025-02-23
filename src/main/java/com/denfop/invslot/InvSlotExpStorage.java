package com.denfop.invslot;

import com.denfop.IUItem;
import com.denfop.api.gui.EnumTypeSlot;
import com.denfop.api.gui.ITypeSlot;
import com.denfop.tiles.mechanism.exp.TileStorageExp;
import net.minecraft.item.ItemStack;

public class InvSlotExpStorage extends InvSlot implements ITypeSlot {

    private final TileStorageExp base1;
    private int stackSizeLimit;

    public InvSlotExpStorage(TileStorageExp base1) {
        super(base1, TypeItemSlot.INPUT, 1);

        this.stackSizeLimit = 1;
        this.base1 = base1;
    }

    @Override
    public EnumTypeSlot getTypeSlot() {
        return EnumTypeSlot.EXP_MODULE;
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

    public boolean accepts(ItemStack itemStack, final int index) {

        return itemStack.getItem().equals(IUItem.expmodule);
    }

    public int getStackSizeLimit() {
        return this.stackSizeLimit;
    }

    public void setStackSizeLimit(int stackSizeLimit) {
        this.stackSizeLimit = stackSizeLimit;
    }

}
