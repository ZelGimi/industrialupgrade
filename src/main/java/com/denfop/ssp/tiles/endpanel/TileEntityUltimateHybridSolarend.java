package com.denfop.ssp.tiles.endpanel;

import com.denfop.ssp.tiles.TileEntityEnderPanel;

public class TileEntityUltimateHybridSolarend extends TileEntityEnderPanel
{
    public static SolarConfig settings;
    
    public TileEntityUltimateHybridSolarend() {
        super(TileEntityUltimateHybridSolarend.settings);
    }
}
