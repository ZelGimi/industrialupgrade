package com.denfop.inventory;

import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.item.energy.EnergyItem;
import com.denfop.utils.ElectricItem;
import com.denfop.utils.ModUtils;
import net.minecraft.world.item.ItemStack;

public class InventoryCharge extends Inventory {

    public int tier;
    private boolean ignore;

    public InventoryCharge(CustomWorldContainer base1, int tier) {
        super(base1, TypeItemSlot.INPUT_OUTPUT, 1);
        this.tier = tier;
        this.ignore = false;
    }

    public void setIgnore(final boolean ignore) {
        this.ignore = ignore;
    }

    public boolean canPlaceItem(int index, ItemStack stack) {
        return stack.getItem() instanceof EnergyItem && ElectricItem.manager.charge(stack, 1.0D / 0.0, this.tier, true,
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
