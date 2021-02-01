package com.denfop.ssp.tiles.panels.overtime;

import com.denfop.ssp.tiles.panels.entity.TileEntitySolarPanel;

public class TileEntityHybridSolar extends TileEntitySolarPanel {

    public static SolarConfig settings;

    public TileEntityHybridSolar() {
        super(TileEntityHybridSolar.settings);
    }

}
