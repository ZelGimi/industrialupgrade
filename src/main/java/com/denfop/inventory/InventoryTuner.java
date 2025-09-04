package com.denfop.inventory;

import com.denfop.api.widget.EnumTypeSlot;
import com.denfop.api.widget.ITypeSlot;
import com.denfop.blockentity.base.BlockEntityInventory;
import com.denfop.items.modules.ItemAdditionModule;
import net.minecraft.world.item.ItemStack;

public class InventoryTuner extends Inventory implements ITypeSlot {

    private int stackSizeLimit;

    public InventoryTuner(BlockEntityInventory base1) {
        super(base1, TypeItemSlot.INPUT_OUTPUT, 1);
        this.stackSizeLimit = 1;
    }

    public boolean canPlaceItem(final int index, ItemStack itemStack) {

        return itemStack.getItem() instanceof ItemAdditionModule && ((ItemAdditionModule<?>) itemStack.getItem()).getElement().getId() == 10;

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
