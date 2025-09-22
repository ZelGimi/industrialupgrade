package com.denfop.invslot;

import com.denfop.ElectricItem;
import com.denfop.api.gui.EnumTypeSlot;
import com.denfop.api.inv.IAdvInventory;
import com.denfop.api.item.IEnergyItem;
import com.denfop.utils.ModUtils;
import net.minecraft.item.ItemStack;

public class InventoryCharge extends Inventory {

    public int tier;
    private boolean ignore;

    public InventoryCharge(IAdvInventory<?> base1, int tier) {
        super(base1, TypeItemSlot.INPUT_OUTPUT, 1);
        this.tier = tier;
        this.ignore = false;
    }

    @Override
    public EnumTypeSlot getTypeSlot() {
        return EnumTypeSlot.BATTERY;
    }

    public void setIgnore(final boolean ignore) {
        this.ignore = ignore;
    }

    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return stack.getItem() instanceof IEnergyItem && ElectricItem.manager.charge(stack, 1.0D / 0.0, this.tier, true,
                true
        ) > 0.0D;
    }

    public double charge(double amount) {
        if (amount <= 0.0D) {
            return 0;
        } else {
            ItemStack stack = this.get(0);
            return ModUtils.isEmpty(stack) ? 0.0D : ElectricItem.manager.charge(stack, amount, this.tier, this.ignore, false);
        }
    }

    public void setTier(int tier1) {
        this.tier = tier1;
    }

}
