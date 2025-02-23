package com.denfop.invslot;

import com.denfop.api.gui.EnumTypeSlot;
import com.denfop.api.gui.ITypeSlot;
import com.denfop.items.modules.ItemAdditionModule;
import com.denfop.tiles.base.TileEntityInventory;
import net.minecraft.item.ItemStack;

public class InvSlotTuner extends InvSlot implements ITypeSlot {

    private int stackSizeLimit;

    public InvSlotTuner(TileEntityInventory base1) {
        super(base1, TypeItemSlot.INPUT_OUTPUT, 1);
        this.stackSizeLimit = 1;
    }

    public boolean accepts(ItemStack itemStack, final int index) {

        return itemStack.getItem() instanceof ItemAdditionModule && itemStack.getItemDamage() == 10;

    }

    @Override
    public EnumTypeSlot getTypeSlot() {
        return EnumTypeSlot.WIRELESS;
    }

    public int getStackSizeLimit() {
        return this.stackSizeLimit;
    }

    public void setStackSizeLimit(int stackSizeLimit) {
        this.stackSizeLimit = stackSizeLimit;
    }

}
