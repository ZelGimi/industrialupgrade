package com.denfop.ssp.tiles.panels.rain;

import com.denfop.ssp.tiles.panels.entity.TileEntityRainPanel;

public class TileEntitySingularrain extends TileEntityRainPanel {

    public static TileEntityRainPanel.SolarConfig settings;

    public TileEntitySingularrain() {
        super(TileEntitySingularrain.settings);
    }

}
