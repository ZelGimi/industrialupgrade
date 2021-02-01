package com.denfop.ssp.tiles.panels.overtime;

import com.denfop.ssp.tiles.panels.entity.TileEntitySolarPanel;

public class TileEntityNeutronSolar extends TileEntitySolarPanel {

    public static TileEntitySolarPanel.SolarConfig settings;

    public TileEntityNeutronSolar() {
        super(TileEntityNeutronSolar.settings);
    }

}
