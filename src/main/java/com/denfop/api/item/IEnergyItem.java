package com.denfop.api.item;

import com.denfop.ElectricItem;
import com.denfop.utils.ModUtils;
import net.minecraft.item.ItemStack;

public interface IEnergyItem {

    boolean canProvideEnergy(ItemStack var1);

    double getMaxEnergy(ItemStack var1);

    short getTierItem(ItemStack var1);

    double getTransferEnergy(ItemStack var1);

    default boolean showDurabilityBar(final ItemStack stack) {
        return true;
    }

    default int getRGBDurabilityForDisplay(ItemStack stack) {
        return ModUtils.convertRGBcolorToInt(33, 91, 199);
    }

    default double getDurabilityForDisplay(ItemStack stack) {
        return Math.min(
                Math.max(
                        1 - ElectricItem.manager.getCharge(stack) / ElectricItem.manager.getMaxCharge(stack),
                        0.0
                ),
                1.0
        );
    }

}
