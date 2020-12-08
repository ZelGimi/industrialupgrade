package com.denfop.ssp.tiles.earthpanel;

import com.denfop.ssp.tiles.TileEntityEarthPanel;

public class TileEntityUltimateHybridSolarearth extends TileEntityEarthPanel
{
    public static SolarConfig settings;
    
    public TileEntityUltimateHybridSolarearth() {
        super(TileEntityUltimateHybridSolarearth.settings);
    }
}
