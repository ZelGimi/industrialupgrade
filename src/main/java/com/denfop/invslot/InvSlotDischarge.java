package com.denfop.invslot;

import com.denfop.tiles.base.IInventorySlotHolder;
import ic2.api.energy.tile.IDischargingSlot;
import ic2.api.info.Info;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.core.util.StackUtil;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class InvSlotDischarge extends InvSlot implements IDischargingSlot {

    public int tier;
    public boolean allowRedstoneDust;

    public InvSlotDischarge(IInventorySlotHolder<?> base, Access access, int tier) {
        this(base, access, tier, InvSide.ANY);
    }

    public InvSlotDischarge(IInventorySlotHolder<?> base, int tier) {
        this(base, Access.I, tier, InvSide.ANY);
    }

    public InvSlotDischarge(IInventorySlotHolder<?> base, Access access, int tier, InvSide preferredSide) {
        this(base, access, tier, true, preferredSide);
    }

    public InvSlotDischarge(
            IInventorySlotHolder<?> base,
            Access access,
            int tier,
            boolean allowRedstoneDust,
            InvSide preferredSide
    ) {
        super(base, "discharge", access, 1, preferredSide);
        this.tier = tier;
        this.allowRedstoneDust = allowRedstoneDust;
    }

    public boolean accepts(ItemStack stack, final int index) {
        if (this.allowRedstoneDust && stack.getItem() == Items.REDSTONE) {
            return true;
        } else if (stack.getItem() instanceof IElectricItem) {
            return ElectricItem.manager.discharge(stack, Integer.MAX_VALUE,
                    this.tier, true,
                    true, true
            ) > 0.0D;
        }
        return false;
    }

    public void setAllowRedstoneDust(final boolean allowRedstoneDust) {
        this.allowRedstoneDust = allowRedstoneDust;
    }

    public double discharge(double amount, boolean ignoreLimit) {
        if (amount <= 0.0D) {
            return 0;
        } else {
            ItemStack stack = this.get(0);
            if (StackUtil.isEmpty(stack)) {
                return 0.0D;
            } else {
                double realAmount = ElectricItem.manager.discharge(stack, amount, this.tier, ignoreLimit, true, false);
                if (realAmount <= 0.0D) {
                    realAmount = Info.itemInfo.getEnergyValue(stack);
                    if (realAmount <= 0.0D) {
                        return 0.0D;
                    }

                    this.put(0, StackUtil.decSize(stack));
                }

                return realAmount;
            }
        }
    }

    public double dischargeWithRedstone(double capacity, double amount) {
        final ItemStack stack = this.get();
        if (stack.isEmpty()) {
            return 0;
        }
        final int size = stack.getCount();
        double canAdd = capacity * 0.2;
        int needRemove = (int) Math.min(amount / canAdd, size);
        if (needRemove <= 0) {
            return 0;
        }
        this.put(0, StackUtil.decSize(stack, needRemove));
        return needRemove * canAdd;

    }

    public void setTier(int tier1) {
        this.tier = tier1;
    }

}
