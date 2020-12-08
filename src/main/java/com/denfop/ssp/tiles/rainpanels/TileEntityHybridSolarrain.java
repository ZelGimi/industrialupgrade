package com.denfop.ssp.tiles.rainpanels;

import com.denfop.ssp.tiles.TileEntityRainPanel;

public class TileEntityHybridSolarrain extends TileEntityRainPanel
{
    public static SolarConfig settings;
    
    public TileEntityHybridSolarrain() {
        super(TileEntityHybridSolarrain.settings);
    }
}
