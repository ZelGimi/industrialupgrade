package com.denfop.ssp.tiles.panels.rain;

import com.denfop.ssp.tiles.panels.entity.TileEntityRainPanel;

public class TileEntityAdminRain extends TileEntityRainPanel {

    public static SolarConfig settings;

    public TileEntityAdminRain() {
        super(TileEntityAdminRain.settings);
    }


}
