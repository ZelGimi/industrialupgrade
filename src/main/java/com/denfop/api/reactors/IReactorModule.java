package com.denfop.api.reactors;

import net.minecraft.item.ItemStack;

public interface IReactorModule {

    double getStableHeat(ItemStack stack);

    double getRadiation(ItemStack stack);

    double getGeneration(ItemStack stack);

    double getComponentVent(ItemStack stack);

    double getVent(ItemStack stack);

    double getExchanger(ItemStack stack);

    double getCapacitor(ItemStack stack);

}
