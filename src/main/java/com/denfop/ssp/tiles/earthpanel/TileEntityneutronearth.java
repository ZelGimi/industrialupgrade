package com.denfop.ssp.tiles.earthpanel;

import com.denfop.ssp.tiles.TileEntityEarthPanel;

public class TileEntityneutronearth extends TileEntityEarthPanel
{
    public static SolarConfig settings;
    
    public TileEntityneutronearth() {
        super(TileEntityneutronearth.settings);
    }
}
