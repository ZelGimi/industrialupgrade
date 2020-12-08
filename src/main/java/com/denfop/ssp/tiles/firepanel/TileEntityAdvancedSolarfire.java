package com.denfop.ssp.tiles.firepanel;

import com.denfop.ssp.tiles.TileEntityNetherPanel;

public class TileEntityAdvancedSolarfire extends TileEntityNetherPanel
{
    public static SolarConfig settings;
    
    public TileEntityAdvancedSolarfire() {
        super(TileEntityAdvancedSolarfire.settings);
    }
}
