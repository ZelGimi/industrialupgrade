package com.denfop.ssp.tiles.Moonpanel;

import com.denfop.ssp.tiles.TileEntityMoonPanel;
import com.denfop.ssp.tiles.TileEntitySolarPanel;
import com.denfop.ssp.tiles.TileEntitySolarPanelsun;

public class TileEntityAdminmoon extends TileEntityMoonPanel
{
    public static SolarConfig settings;
    
    public TileEntityAdminmoon() {
        super(TileEntityAdminmoon.settings);
    }

	
}
