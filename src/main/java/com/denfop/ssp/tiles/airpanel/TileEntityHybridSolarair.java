package com.denfop.ssp.tiles.airpanel;

import com.denfop.ssp.tiles.TileEntityAirPanel;

public class TileEntityHybridSolarair extends TileEntityAirPanel
{
    public static SolarConfig settings;
    
    public TileEntityHybridSolarair() {
        super(TileEntityHybridSolarair.settings);
    }
}
