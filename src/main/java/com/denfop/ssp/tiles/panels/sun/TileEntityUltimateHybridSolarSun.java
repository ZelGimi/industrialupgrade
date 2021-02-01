package com.denfop.ssp.tiles.panels.sun;

import com.denfop.ssp.tiles.panels.entity.TileEntitySunPanel;

public class TileEntityUltimateHybridSolarSun extends TileEntitySunPanel {

    public static SolarConfig settings;

    public TileEntityUltimateHybridSolarSun() {
        super(TileEntityUltimateHybridSolarSun.settings);
    }

}
