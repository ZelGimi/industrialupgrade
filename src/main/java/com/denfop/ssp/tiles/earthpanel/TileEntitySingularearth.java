package com.denfop.ssp.tiles.earthpanel;

import com.denfop.ssp.tiles.TileEntityEarthPanel;

public class TileEntitySingularearth extends TileEntityEarthPanel
{
    public static TileEntityEarthPanel.SolarConfig settings;
    
    public TileEntitySingularearth() {
        super(TileEntitySingularearth.settings);
    }
}
