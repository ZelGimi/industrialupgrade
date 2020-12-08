package com.Denfop.ssp.tiles.airpanel;

import com.Denfop.ssp.tiles.TileEntityAirPanel;
import com.Denfop.ssp.tiles.TileEntityMoonPanel;
import com.Denfop.ssp.tiles.TileEntityMoonPanel1;
import com.Denfop.ssp.tiles.TileEntitySolarPanel;
import com.Denfop.ssp.tiles.TileEntitySolarPanelsun;

public class TileEntityAdminair extends TileEntityAirPanel
{
    public static SolarConfig settings;
    
    public TileEntityAdminair() {
        super(TileEntityAdminair.settings);
    }

	
}
