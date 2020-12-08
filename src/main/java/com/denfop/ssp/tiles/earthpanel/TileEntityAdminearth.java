package com.denfop.ssp.tiles.earthpanel;

import com.denfop.ssp.tiles.TileEntityEarthPanel;

public class TileEntityAdminearth extends TileEntityEarthPanel
{
    public static SolarConfig settings;
    
    public TileEntityAdminearth() {
        super(TileEntityAdminearth.settings);
    }

	
}
