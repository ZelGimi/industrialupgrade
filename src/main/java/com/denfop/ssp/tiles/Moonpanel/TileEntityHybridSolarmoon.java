package com.denfop.ssp.tiles.Moonpanel;

import com.denfop.ssp.tiles.TileEntityMoonPanel;

public class TileEntityHybridSolarmoon extends TileEntityMoonPanel
{
    public static SolarConfig settings;
    
    public TileEntityHybridSolarmoon() {
        super(TileEntityHybridSolarmoon.settings);
    }
}
