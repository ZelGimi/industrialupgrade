package com.denfop.ssp.tiles.rainpanels;

import com.denfop.ssp.tiles.TileEntityRainPanel;

public class TileEntityneutronrain extends TileEntityRainPanel
{
    public static SolarConfig settings;
    
    public TileEntityneutronrain() {
        super(TileEntityneutronrain.settings);
    }
}
