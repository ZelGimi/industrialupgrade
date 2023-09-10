package com.denfop.api.upgrades;

import net.minecraft.item.ItemStack;

import java.util.Set;

public interface IUpgradeItem {

    boolean isSuitableFor(ItemStack var1, Set<UpgradableProperty> var2);

    double getEnergyDemandMultiplier(final ItemStack itemStack);

    double getProcessTimeMultiplier(final ItemStack itemStack);

    int getExtraTier(final ItemStack itemStack);

    double getExtraEnergyStorage(ItemStack stack);

}
