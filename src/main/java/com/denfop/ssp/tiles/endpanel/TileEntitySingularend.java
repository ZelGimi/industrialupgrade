package com.denfop.ssp.tiles.endpanel;

import com.denfop.ssp.tiles.TileEntityEnderPanel;

public class TileEntitySingularend extends TileEntityEnderPanel
{
    public static TileEntityEnderPanel.SolarConfig settings;
    
    public TileEntitySingularend() {
        super(TileEntitySingularend.settings);
    }
}
