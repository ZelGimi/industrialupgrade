package com.denfop.ssp.tiles.panels.overtime;

import com.denfop.ssp.tiles.panels.entity.TileEntitySolarPanel;

public class TileEntityAdvancedSolar extends TileEntitySolarPanel {

    public static SolarConfig settings;

    public TileEntityAdvancedSolar() {
        super(TileEntityAdvancedSolar.settings);
    }

}
