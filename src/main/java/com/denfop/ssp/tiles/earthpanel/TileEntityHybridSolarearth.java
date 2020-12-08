package com.denfop.ssp.tiles.earthpanel;

import com.denfop.ssp.tiles.TileEntityEarthPanel;

public class TileEntityHybridSolarearth extends TileEntityEarthPanel
{
    public static SolarConfig settings;
    
    public TileEntityHybridSolarearth() {
        super(TileEntityHybridSolarearth.settings);
    }
}
