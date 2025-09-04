package com.denfop.api.solar;


import com.denfop.blockentity.panels.entity.BlockEntityMiniPanels;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public interface ISolarTile {

    void setCapacity(double capacity);

    void setOutput(double output);

    void setGeneration(EnumSolarType solarType, double generation);

    List<ItemStack> getCapacityItems();

    List<ItemStack> getOutputItems();

    List<ItemStack> getGenerationItems();

    void setBonus(EnumTypeParts typeBonus, double bonus);

    double getBonus(EnumTypeParts typeBonus);


    void setLoad(double load);

    List<List<BlockEntityMiniPanels.EnumState>> getStables();

    void setStables(int index, List<BlockEntityMiniPanels.EnumState> enumStateList);

    List<ItemStack> getCoresItems();

    int getCoreLevel();

    void setCoreLevel(int level);
}
