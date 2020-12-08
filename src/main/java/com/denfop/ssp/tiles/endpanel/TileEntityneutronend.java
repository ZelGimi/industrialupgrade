package com.denfop.ssp.tiles.endpanel;

import com.denfop.ssp.tiles.TileEntityEnderPanel;

public class TileEntityneutronend extends TileEntityEnderPanel
{
    public static SolarConfig settings;
    
    public TileEntityneutronend() {
        super(TileEntityneutronend.settings);
    }
}
