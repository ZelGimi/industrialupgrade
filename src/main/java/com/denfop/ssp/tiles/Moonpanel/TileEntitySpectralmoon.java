package com.denfop.ssp.tiles.Moonpanel;

import com.denfop.ssp.tiles.TileEntityMoonPanel;

public class TileEntitySpectralmoon extends TileEntityMoonPanel
{
    public static TileEntityMoonPanel.SolarConfig settings;
    
    public TileEntitySpectralmoon() {
        super(TileEntitySpectralmoon.settings);
    }
}
