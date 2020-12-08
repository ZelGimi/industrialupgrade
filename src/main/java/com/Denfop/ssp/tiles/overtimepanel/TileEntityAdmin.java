package com.Denfop.ssp.tiles.overtimepanel;

import com.Denfop.ssp.tiles.TileEntityAirPanel;
import com.Denfop.ssp.tiles.TileEntityEarthPanel;
import com.Denfop.ssp.tiles.TileEntityNetherPanel;
import com.Denfop.ssp.tiles.TileEntitySolarPanel;

public class TileEntityAdmin extends TileEntitySolarPanel
{
    public static TileEntitySolarPanel.SolarConfig settings;
    
    public TileEntityAdmin() {
        super(TileEntityAdmin.settings);
    }

	
}
