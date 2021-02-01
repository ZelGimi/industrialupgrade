package com.denfop.ssp.tiles.panels.rain;

import com.denfop.ssp.tiles.panels.entity.TileEntityRainPanel;

public class TileEntityQuantumSolarRain extends TileEntityRainPanel {

    public static SolarConfig settings;

    public TileEntityQuantumSolarRain() {
        super(TileEntityQuantumSolarRain.settings);
    }

}
