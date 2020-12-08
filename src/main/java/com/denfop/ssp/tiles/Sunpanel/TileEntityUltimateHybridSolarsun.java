package com.denfop.ssp.tiles.Sunpanel;

import com.denfop.ssp.tiles.TileEntitySolarPanelsun;

public class TileEntityUltimateHybridSolarsun extends TileEntitySolarPanelsun
{
    public static SolarConfig settings;
    
    public TileEntityUltimateHybridSolarsun() {
        super(TileEntityUltimateHybridSolarsun.settings);
    }
}
