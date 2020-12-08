package com.denfop.ssp.tiles.rainpanels;

import com.denfop.ssp.tiles.TileEntityRainPanel;

public class TileEntitySingularrain extends TileEntityRainPanel
{
    public static TileEntityRainPanel.SolarConfig settings;
    
    public TileEntitySingularrain() {
        super(TileEntitySingularrain.settings);
    }
}
