package com.denfop.api.solar;

import net.minecraft.item.ItemStack;

import java.util.List;

public interface ISolarTile {

    void setCapacity(double capacity);

    void setOutput(double output);

    void setGeneration(EnumSolarType solarType, double generation);

    List<ItemStack> getCapacityItems();

    List<ItemStack> getOutputItems();

    List<ItemStack> getGenerationItems();

    void setBonus(EnumTypeParts typeBonus, double bonus);

    void setLoad(double load);

}
