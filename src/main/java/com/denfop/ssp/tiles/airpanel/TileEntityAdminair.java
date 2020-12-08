package com.denfop.ssp.tiles.airpanel;

import com.denfop.ssp.tiles.TileEntityAirPanel;
import com.denfop.ssp.tiles.TileEntityMoonPanel;
import com.denfop.ssp.tiles.TileEntityMoonPanel1;
import com.denfop.ssp.tiles.TileEntitySolarPanel;
import com.denfop.ssp.tiles.TileEntitySolarPanelsun;

public class TileEntityAdminair extends TileEntityAirPanel
{
    public static SolarConfig settings;
    
    public TileEntityAdminair() {
        super(TileEntityAdminair.settings);
    }

	
}
