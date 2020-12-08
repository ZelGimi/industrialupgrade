package com.denfop.ssp.tiles.endpanel;

import com.denfop.ssp.tiles.TileEntityEnderPanel;

public class TileEntityAdminend extends TileEntityEnderPanel
{
    public static SolarConfig settings;
    
    public TileEntityAdminend() {
        super(TileEntityAdminend.settings);
    }

	
}
