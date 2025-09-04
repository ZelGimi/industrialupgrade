package com.denfop.api.item.energy;

import com.denfop.utils.ElectricItem;
import com.denfop.utils.ModUtils;
import net.minecraft.world.item.ItemStack;

public interface EnergyItem {

    boolean canProvideEnergy(ItemStack var1);

    double getMaxEnergy(ItemStack var1);

    short getTierItem(ItemStack var1);

    double getTransferEnergy(ItemStack var1);

    default boolean isBarVisible(final ItemStack stack) {
        return true;
    }

    default int getBarColor(ItemStack stack) {
        return ModUtils.convertRGBcolorToInt(33, 91, 199);
    }


    default int getBarWidth(ItemStack stack) {

        return 13 - (int) (13.0F * Math.min(
                Math.max(
                        1 - ElectricItem.manager.getCharge(stack) / ElectricItem.manager.getMaxCharge(stack),
                        0.0
                ),
                1.0
        ));
    }

}
