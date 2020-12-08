package com.Denfop.ssp.tiles.Sunpanel;

import com.Denfop.ssp.tiles.TileEntitySolarPanel;
import com.Denfop.ssp.tiles.TileEntitySolarPanelsun;

public class TileEntityAdminsun extends TileEntitySolarPanelsun
{
    public static TileEntitySolarPanelsun.SolarConfig settings;
    
    public TileEntityAdminsun() {
        super(TileEntityAdminsun.settings);
    }

	
}
