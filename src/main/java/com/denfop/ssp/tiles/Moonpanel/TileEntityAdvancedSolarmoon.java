package com.denfop.ssp.tiles.Moonpanel;

import com.denfop.ssp.tiles.TileEntityMoonPanel;
import com.denfop.ssp.tiles.TileEntitySolarPanel;
import com.denfop.ssp.tiles.TileEntitySolarPanelsun;

public class TileEntityAdvancedSolarmoon extends TileEntityMoonPanel
{
    public static SolarConfig settings;
    
    public TileEntityAdvancedSolarmoon() {
        super(TileEntityAdvancedSolarmoon.settings);
    }
}
