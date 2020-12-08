package com.denfop.ssp.tiles.overtimepanel;

import com.denfop.ssp.tiles.TileEntityAirPanel;
import com.denfop.ssp.tiles.TileEntityEarthPanel;
import com.denfop.ssp.tiles.TileEntityNetherPanel;
import com.denfop.ssp.tiles.TileEntitySolarPanel;

public class TileEntityAdmin extends TileEntitySolarPanel
{
    public static TileEntitySolarPanel.SolarConfig settings;
    
    public TileEntityAdmin() {
        super(TileEntityAdmin.settings);
    }

	
}
