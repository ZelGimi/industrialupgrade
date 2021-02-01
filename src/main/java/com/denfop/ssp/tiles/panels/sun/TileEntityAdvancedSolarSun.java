package com.denfop.ssp.tiles.panels.sun;

import com.denfop.ssp.tiles.panels.entity.TileEntitySunPanel;

public class TileEntityAdvancedSolarSun extends TileEntitySunPanel {

    public static SolarConfig settings;

    public TileEntityAdvancedSolarSun() {
        super(TileEntityAdvancedSolarSun.settings);
    }

}
