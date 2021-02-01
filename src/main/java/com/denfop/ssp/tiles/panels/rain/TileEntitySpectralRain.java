package com.denfop.ssp.tiles.panels.rain;

import com.denfop.ssp.tiles.panels.entity.TileEntityRainPanel;

public class TileEntitySpectralRain extends TileEntityRainPanel {

    public static TileEntityRainPanel.SolarConfig settings;

    public TileEntitySpectralRain() {
        super(TileEntitySpectralRain.settings);
    }

}
