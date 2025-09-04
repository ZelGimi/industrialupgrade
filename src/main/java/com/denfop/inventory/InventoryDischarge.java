package com.denfop.inventory;

import com.denfop.api.container.CustomWorldContainer;
import com.denfop.api.item.energy.EnergyItem;
import com.denfop.utils.ElectricItem;
import com.denfop.utils.ModUtils;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class InventoryDischarge extends Inventory {

    public int tier;
    public boolean allowRedstoneDust;

    public InventoryDischarge(CustomWorldContainer base, TypeItemSlot typeItemSlot, int tier) {
        this(base, typeItemSlot, tier, false);
    }

    public InventoryDischarge(CustomWorldContainer base, int tier) {
        this(base, TypeItemSlot.INPUT, tier);
    }


    public InventoryDischarge(
            CustomWorldContainer base,
            TypeItemSlot typeItemSlot,
            int tier,
            boolean allowRedstoneDust
    ) {
        super(base, typeItemSlot, 1);
        this.tier = tier;
        this.allowRedstoneDust = allowRedstoneDust;
    }

    public boolean canPlaceItem(final int index, ItemStack stack) {
        if (this.allowRedstoneDust && stack.getItem() == Items.REDSTONE) {
            return true;
        } else if (stack.getItem() instanceof EnergyItem) {
            return ElectricItem.manager.discharge(stack, Integer.MAX_VALUE,
                    this.tier, true,
                    true, true
            ) > 0.0D;
        }
        return false;
    }

    public double discharge(double amount, boolean ignoreLimit) {
        if (amount <= 0.0D) {
            return 0;
        } else {
            ItemStack stack = this.get(0);
            if (ModUtils.isEmpty(stack)) {
                return 0.0D;
            } else {
                double realAmount = ElectricItem.manager.discharge(stack, amount, this.tier, ignoreLimit, true, false);
                if (realAmount <= 0.0D) {
                    realAmount = ModUtils.getEnergyValue(stack);
                    if (realAmount <= 0.0D) {
                        return 0.0D;
                    }
                    stack.shrink(1);
                    this.set(0, stack);
                }
                return realAmount;
            }
        }
    }

    public double dischargeWithRedstone(double capacity, double amount) {
        final ItemStack stack = this.get(0);
        if (stack.isEmpty()) {
            return 0;
        }
        final int size = stack.getCount();
        double canAdd = capacity * 0.2;
        int needRemove = (int) Math.min(amount / canAdd, size);
        if (needRemove <= 0) {
            return 0;
        }
        this.set(0, ModUtils.decSize(stack, needRemove));
        return needRemove * canAdd;

    }

    public void setTier(int tier1) {
        this.tier = tier1;
    }

}
