package com.denfop.ssp.tiles.Sunpanel;

import com.denfop.ssp.tiles.TileEntitySolarPanelsun;

public class TileEntityAdvancedSolarsun extends TileEntitySolarPanelsun
{
    public static SolarConfig settings;
    
    public TileEntityAdvancedSolarsun() {
        super(TileEntityAdvancedSolarsun.settings);
    }
}
