package com.denfop.ssp.tiles.earthpanel;

import com.denfop.ssp.tiles.TileEntityEarthPanel;

public class TileEntityAdvancedSolarearth extends TileEntityEarthPanel
{
    public static SolarConfig settings;
    
    public TileEntityAdvancedSolarearth() {
        super(TileEntityAdvancedSolarearth.settings);
    }
}
