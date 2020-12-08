package com.denfop.ssp.tiles.Moonpanel;

import com.denfop.ssp.tiles.TileEntityMoonPanel;
import com.denfop.ssp.tiles.TileEntitySolarPanel;
import com.denfop.ssp.tiles.TileEntitySolarPanelsun;

public class TileEntityHybridSolarmoon extends TileEntityMoonPanel
{
    public static SolarConfig settings;
    
    public TileEntityHybridSolarmoon() {
        super(TileEntityHybridSolarmoon.settings);
    }
}
