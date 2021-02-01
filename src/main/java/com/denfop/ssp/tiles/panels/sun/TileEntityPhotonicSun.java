package com.denfop.ssp.tiles.panels.sun;

import com.denfop.ssp.tiles.panels.entity.TileEntitySunPanel;

public class TileEntityPhotonicSun extends TileEntitySunPanel {

    public static TileEntitySunPanel.SolarConfig settings;

    public TileEntityPhotonicSun() {
        super(TileEntityPhotonicSun.settings);
    }


}
