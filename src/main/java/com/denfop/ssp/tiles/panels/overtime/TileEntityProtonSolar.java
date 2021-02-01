package com.denfop.ssp.tiles.panels.overtime;

import com.denfop.ssp.tiles.panels.entity.TileEntitySolarPanel;

public class TileEntityProtonSolar extends TileEntitySolarPanel {

    public static TileEntitySolarPanel.SolarConfig settings;

    public TileEntityProtonSolar() {
        super(TileEntityProtonSolar.settings);
    }

}
