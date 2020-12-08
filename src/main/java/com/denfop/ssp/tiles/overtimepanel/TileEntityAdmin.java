package com.denfop.ssp.tiles.overtimepanel;

import com.denfop.ssp.tiles.TileEntitySolarPanel;

public class TileEntityAdmin extends TileEntitySolarPanel
{
    public static TileEntitySolarPanel.SolarConfig settings;
    
    public TileEntityAdmin() {
        super(TileEntityAdmin.settings);
    }

	
}
