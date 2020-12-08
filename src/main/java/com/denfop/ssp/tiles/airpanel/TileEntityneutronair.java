package com.denfop.ssp.tiles.airpanel;

import com.denfop.ssp.tiles.TileEntityAirPanel;

public class TileEntityneutronair extends TileEntityAirPanel
{
    public static SolarConfig settings;
    
    public TileEntityneutronair() {
        super(TileEntityneutronair.settings);
    }
}
