package com.denfop.inventory;

import com.denfop.api.widget.EnumTypeSlot;
import com.denfop.api.widget.ITypeSlot;
import com.denfop.blockentity.base.BlockEntityInventory;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.ItemStack;

public class InventoryFisher extends Inventory implements ITypeSlot {

    private int stackSizeLimit;

    public InventoryFisher(BlockEntityInventory base1) {
        super(base1, TypeItemSlot.INPUT, 1);
        this.stackSizeLimit = 1;
    }

    @Override
    public EnumTypeSlot getTypeSlot() {
        return EnumTypeSlot.FISHING_ROD;
    }

    public boolean canPlaceItem(final int index, ItemStack itemStack) {
        return itemStack.getItem() instanceof FishingRodItem;
    }

    public int getStackSizeLimit() {
        return this.stackSizeLimit;
    }

    public void setStackSizeLimit(int stackSizeLimit) {
        this.stackSizeLimit = stackSizeLimit;
    }

    public void consume(int amount) {
        consume(amount, false);
    }

    public void consume(int amount, boolean simulate) {
        for (int i = 0; i < size(); i++) {
            ItemStack stack = get(i);
            if (!stack.isEmpty() && amount > 0) {
                int currentAmount = Math.min(amount, stack.getCount());
                if (!simulate) {
                    stack.shrink(currentAmount);
                }
                amount -= currentAmount;
                if (amount == 0) {
                    break;
                }
            }
        }
    }

}
