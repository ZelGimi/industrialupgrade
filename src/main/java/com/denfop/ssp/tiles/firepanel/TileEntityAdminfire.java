package com.denfop.ssp.tiles.firepanel;

import com.denfop.ssp.tiles.TileEntityNetherPanel;

public class TileEntityAdminfire extends TileEntityNetherPanel
{
    public static SolarConfig settings;
    
    public TileEntityAdminfire() {
        super(TileEntityAdminfire.settings);
    }

	
}
