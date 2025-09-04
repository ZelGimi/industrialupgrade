package com.denfop.utils;

import com.denfop.api.item.energy.EnergyItem;
import com.denfop.datacomponent.DataComponentsInit;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class ElectricItemManager implements com.denfop.api.item.energy.ElectricItemManager {

    public ElectricItemManager() {
    }

    public static ItemStack getCharged(Item item, double charge) {
        if (!(item instanceof EnergyItem)) {
            throw new IllegalArgumentException("no electric item");
        } else {
            ItemStack ret = new ItemStack(item).copy();
            ElectricItem.manager.charge(ret, charge, 2147483647, true, false);
            return ret;
        }
    }

    public static void addChargeVariants(Item item, List<ItemStack> list) {
        list.add(getCharged(item, 0.0D));
        list.add(getCharged(item, Double.MAX_VALUE));
    }

    public double charge(ItemStack stack, double amount, int tier, boolean ignoreTransferLimit, boolean simulate) {
        EnergyItem item = (EnergyItem) stack.getItem();

        assert item.getMaxEnergy(stack) > 0.0D;
        if (this.getCharge(stack) == item.getMaxEnergy(stack)) {
            return 0;
        }
        if (!(amount < 0.0D) && ModUtils.getSize(stack) <= 1 && item.getTierItem(stack) <= tier) {
            if (!ignoreTransferLimit && amount > item.getTransferEnergy(stack)) {
                amount = item.getTransferEnergy(stack);
            }
            double newCharge = stack.getOrDefault(DataComponentsInit.ENERGY, 0).doubleValue();
            amount = Math.min(amount, item.getMaxEnergy(stack) - newCharge);
            if (!simulate) {
                newCharge += amount;
                stack.set(DataComponentsInit.ENERGY, Math.max(newCharge, 0.0D));
            }

            return amount;
        } else {
            return 0.0D;
        }
    }

    public double discharge(
            ItemStack stack,
            double amount,
            int tier,
            boolean ignoreTransferLimit,
            boolean externally,
            boolean simulate
    ) {
        EnergyItem item = (EnergyItem) stack.getItem();

        assert item.getMaxEnergy(stack) > 0.0D;
        if (!(amount < 0.0D) && ModUtils.getSize(stack) <= 1 && item.getTierItem(stack) <= tier) {
            if (externally && !item.canProvideEnergy(stack)) {
                return 0.0D;
            } else {
                if (!ignoreTransferLimit && amount > item.getTransferEnergy(stack)) {
                    amount = item.getTransferEnergy(stack);
                }


                double newCharge = getCharge(stack);
                amount = Math.min(amount, newCharge);
                if (!simulate) {
                    newCharge -= amount;
                    stack.set(DataComponentsInit.ENERGY, Math.max(newCharge, 0.0D));
                }
                return amount;
            }
        } else {
            return 0.0D;
        }
    }

    public double getCharge(ItemStack stack) {
        if (!stack.has(DataComponentsInit.ENERGY))
            stack.set(DataComponentsInit.ENERGY, 0d);
        return stack.get(DataComponentsInit.ENERGY).doubleValue();
    }

    public double getMaxCharge(ItemStack stack) {
        return ((EnergyItem) stack.getItem()).getMaxEnergy(stack);
    }

    public boolean canUse(ItemStack stack, double amount) {
        return ElectricItem.manager.getCharge(stack) >= amount;
    }

    public boolean use(ItemStack stack, double amount, LivingEntity entity) {

        double transfer = this.getCharge(stack);
        if (transfer >= amount) {
            ElectricItem.manager.discharge(stack, amount, 2147483647, true, false, false);
            return true;
        } else {
            return false;
        }
    }


    public String getToolTip(ItemStack stack) {
        if (stack.getItem() instanceof EnergyItem) {
            double charge = ElectricItem.manager.getCharge(stack);
            return ModUtils.getString(charge) + "/" + ModUtils.getString(
                    ((EnergyItem) stack.getItem()).getMaxEnergy(stack)
            ) + " EF";
        } else {
            return "";
        }
    }

    public int getTier(ItemStack stack) {
        return !stack.isEmpty() && stack.getItem() instanceof EnergyItem
                ? ((EnergyItem) stack.getItem()).getTierItem(stack)
                : 0;
    }

}
