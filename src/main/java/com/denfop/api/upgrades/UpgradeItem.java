package com.denfop.api.upgrades;


import net.minecraft.world.item.ItemStack;

import java.util.Set;

public interface UpgradeItem {

    boolean isSuitableFor(ItemStack var1, Set<EnumBlockEntityUpgrade> var2);

    double getEnergyDemandMultiplier(final ItemStack itemStack);

    double getProcessTimeMultiplier(final ItemStack itemStack);

    int getExtraTier(final ItemStack itemStack);

    double getExtraEnergyStorage(ItemStack stack);

}
