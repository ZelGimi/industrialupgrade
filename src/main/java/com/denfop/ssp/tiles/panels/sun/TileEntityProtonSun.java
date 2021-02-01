package com.denfop.ssp.tiles.panels.sun;

import com.denfop.ssp.tiles.panels.entity.TileEntitySunPanel;

public class TileEntityProtonSun extends TileEntitySunPanel {

    public static TileEntitySunPanel.SolarConfig settings;

    public TileEntityProtonSun() {
        super(TileEntityProtonSun.settings);
    }

}
