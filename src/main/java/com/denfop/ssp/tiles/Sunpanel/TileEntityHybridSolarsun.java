package com.denfop.ssp.tiles.Sunpanel;

import com.denfop.ssp.tiles.TileEntitySolarPanelsun;

public class TileEntityHybridSolarsun extends TileEntitySolarPanelsun
{
    public static SolarConfig settings;
    
    public TileEntityHybridSolarsun() {
        super(TileEntityHybridSolarsun.settings);
    }
}
