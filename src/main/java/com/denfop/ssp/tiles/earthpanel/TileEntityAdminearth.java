package com.denfop.ssp.tiles.earthpanel;

import com.denfop.ssp.tiles.TileEntityEarthPanel;
import com.denfop.ssp.tiles.TileEntityMoonPanel;
import com.denfop.ssp.tiles.TileEntityMoonPanel1;
import com.denfop.ssp.tiles.TileEntitySolarPanel;
import com.denfop.ssp.tiles.TileEntitySolarPanelsun;

public class TileEntityAdminearth extends TileEntityEarthPanel
{
    public static SolarConfig settings;
    
    public TileEntityAdminearth() {
        super(TileEntityAdminearth.settings);
    }

	
}
