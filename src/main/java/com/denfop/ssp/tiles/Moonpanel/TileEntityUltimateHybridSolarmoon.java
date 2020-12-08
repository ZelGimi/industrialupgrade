package com.denfop.ssp.tiles.Moonpanel;

import com.denfop.ssp.tiles.TileEntityMoonPanel;

public class TileEntityUltimateHybridSolarmoon extends TileEntityMoonPanel
{
    public static SolarConfig settings;
    
    public TileEntityUltimateHybridSolarmoon() {
        super(TileEntityUltimateHybridSolarmoon.settings);
    }
}
