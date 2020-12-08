package com.denfop.ssp.tiles.Moonpanel;

import com.denfop.ssp.tiles.TileEntityMoonPanel;
import com.denfop.ssp.tiles.TileEntitySolarPanel;
import com.denfop.ssp.tiles.TileEntitySolarPanelsun;

public class TileEntitySingularmoon extends TileEntityMoonPanel
{
    public static TileEntityMoonPanel.SolarConfig settings;
    
    public TileEntitySingularmoon() {
        super(TileEntitySingularmoon.settings);
    }
}
