package com.Denfop.ssp.tiles.earthpanel;

import com.Denfop.ssp.tiles.TileEntityEarthPanel;
import com.Denfop.ssp.tiles.TileEntityMoonPanel;
import com.Denfop.ssp.tiles.TileEntityMoonPanel1;
import com.Denfop.ssp.tiles.TileEntitySolarPanel;
import com.Denfop.ssp.tiles.TileEntitySolarPanelsun;

public class TileEntityAdminearth extends TileEntityEarthPanel
{
    public static SolarConfig settings;
    
    public TileEntityAdminearth() {
        super(TileEntityAdminearth.settings);
    }

	
}
