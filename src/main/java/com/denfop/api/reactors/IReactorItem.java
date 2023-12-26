package com.denfop.api.reactors;

import net.minecraft.item.ItemStack;

public interface IReactorItem {

    EnumTypeComponent getType();

    default double getRadiation(){
        return 0;
    }

    int getLevel();

    int getAutoRepair(final IAdvReactor reactor);

    int getRepairOther(final IAdvReactor reactor);

    int getDamageCFromHeat(int heat, final IAdvReactor reactor);

    int getHeat(final IAdvReactor reactor);

    double getHeatRemovePercent(final IAdvReactor reactor);

    void damageItem(ItemStack stack, int damage);

    boolean updatableItem();

    boolean caneExtractHeat();

    double getEnergyProduction(final IAdvReactor reactor);

    boolean needClear(ItemStack stack);
}
