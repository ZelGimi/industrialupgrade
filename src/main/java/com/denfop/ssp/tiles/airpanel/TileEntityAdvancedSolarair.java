package com.denfop.ssp.tiles.airpanel;

import com.denfop.ssp.tiles.TileEntityAirPanel;

public class TileEntityAdvancedSolarair extends TileEntityAirPanel
{
    public static SolarConfig settings;
    
    public TileEntityAdvancedSolarair() {
        super(TileEntityAdvancedSolarair.settings);
    }
}
