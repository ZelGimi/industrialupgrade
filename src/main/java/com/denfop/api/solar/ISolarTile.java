package com.denfop.api.solar;

import com.denfop.tiles.panels.entity.TileEntityMiniPanels;
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

    double getBonus(EnumTypeParts typeBonus);


    void setLoad(double load);

    List<List<TileEntityMiniPanels.EnumState>> getStables();

    void setStables(int index, List<TileEntityMiniPanels.EnumState> enumStateList);

    void  setCoreLevel(int level);

    List<ItemStack> getCoresItems();

    int getCoreLevel();
}
