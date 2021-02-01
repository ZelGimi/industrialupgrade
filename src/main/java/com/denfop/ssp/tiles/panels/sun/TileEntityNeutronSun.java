package com.denfop.ssp.tiles.panels.sun;

import com.denfop.ssp.tiles.panels.entity.TileEntitySunPanel;

public class TileEntityNeutronSun extends TileEntitySunPanel {

    public static TileEntitySunPanel.SolarConfig settings;

    public TileEntityNeutronSun() {
        super(TileEntityNeutronSun.settings);
    }

}
