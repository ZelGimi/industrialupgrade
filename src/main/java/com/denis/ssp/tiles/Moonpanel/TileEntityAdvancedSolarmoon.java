package com.Denfop.ssp.tiles.Moonpanel;

import com.Denfop.ssp.tiles.TileEntityMoonPanel;
import com.Denfop.ssp.tiles.TileEntitySolarPanel;
import com.Denfop.ssp.tiles.TileEntitySolarPanelsun;

public class TileEntityAdvancedSolarmoon extends TileEntityMoonPanel
{
    public static SolarConfig settings;
    
    public TileEntityAdvancedSolarmoon() {
        super(TileEntityAdvancedSolarmoon.settings);
    }
}
