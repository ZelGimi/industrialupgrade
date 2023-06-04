package com.denfop.tiles.panels.entity;

import com.denfop.api.energy.IAdvEnergyTile;
import com.denfop.componets.ComponentsForgeEnergy;

public class ComponentsSolarForgeEnergy extends ComponentsForgeEnergy {

    private final TileEntitySolarPanel source;

    public ComponentsSolarForgeEnergy(
            final TileEntitySolarPanel inventory,
            final boolean isSink,
            final boolean isSource,
            final IAdvEnergyTile tile
    ) {
        super(null, isSink, isSource, tile);
        this.source = inventory;
    }

    @Override
    public int getEnergyStored() {
        if (source.storage * 4 > Integer.MAX_VALUE) {
            return Integer.MAX_VALUE - 1;
        }
        return (int) source.storage * 4;
    }

    @Override
    public int getMaxEnergyStored() {
        if (source.maxStorage * 4 > Integer.MAX_VALUE) {
            return Integer.MAX_VALUE - 1;
        }
        return (int) source.maxStorage * 4;
    }

}
