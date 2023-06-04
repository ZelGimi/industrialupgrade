package com.denfop.invslot;

import com.denfop.tiles.base.IInventorySlotHolder;
import ic2.api.energy.tile.IChargingSlot;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.core.util.StackUtil;
import net.minecraft.item.ItemStack;

public class InvSlotCharge extends InvSlot implements IChargingSlot {

    public int tier;
    private boolean ignore;

    public InvSlotCharge(IInventorySlotHolder<?> base1, int tier) {
        super(base1, "charge", Access.IO, 1, InvSide.ANY);
        this.tier = tier;
        this.ignore = false;
    }

    public void setIgnore(final boolean ignore) {
        this.ignore = ignore;
    }

    public boolean accepts(ItemStack stack, int index) {
        return stack.getItem() instanceof IElectricItem && ElectricItem.manager.charge(stack, 1.0D / 0.0, this.tier, true,
                true
        ) > 0.0D;
    }

    public double charge(double amount) {
        if (amount <= 0.0D) {
            return 0;
        } else {
            ItemStack stack = this.get(0);
            return StackUtil.isEmpty(stack) ? 0.0D : ElectricItem.manager.charge(stack, amount, this.tier, this.ignore, false);
        }
    }

    public void setTier(int tier1) {
        this.tier = tier1;
    }

}
