package com.denfop.ssp.tiles.panels.rain;

import com.denfop.ssp.tiles.panels.entity.TileEntityRainPanel;

public class TileEntityHybridSolarrain extends TileEntityRainPanel {

    public static SolarConfig settings;

    public TileEntityHybridSolarrain() {
        super(TileEntityHybridSolarrain.settings);
    }

}
