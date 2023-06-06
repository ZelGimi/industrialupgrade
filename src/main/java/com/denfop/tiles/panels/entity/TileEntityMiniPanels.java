package com.denfop.tiles.panels.entity;

import com.denfop.api.solar.EnumSolarType;
import com.denfop.api.solar.EnumTypeParts;
import com.denfop.api.solar.ISolarTile;
import com.denfop.tiles.base.TileEntityInventory;
import net.minecraft.item.ItemStack;

import java.util.List;

public class TileEntityMiniPanels extends TileEntityInventory implements ISolarTile {

    @Override
    public void setCapacity(final double capacity) {

    }

    @Override
    public void setOutput(final double output) {

    }

    @Override
    public void setGeneration(final EnumSolarType solarType, final double generation) {

    }

    @Override
    public List<ItemStack> getCapacityItems() {
        return null;
    }

    @Override
    public List<ItemStack> getOutputItems() {
        return null;
    }

    @Override
    public List<ItemStack> getGenerationItems() {
        return null;
    }

    @Override
    public void setBonus(final EnumTypeParts typeBonus, final double bonus) {

    }

    @Override
    public void setLoad(final double load) {

    }

}
