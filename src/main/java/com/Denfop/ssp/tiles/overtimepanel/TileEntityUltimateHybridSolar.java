package com.Denfop.ssp.tiles.overtimepanel;

import com.Denfop.ssp.tiles.TileEntitySolarPanel;

public class TileEntityUltimateHybridSolar extends TileEntitySolarPanel
{
    public static SolarConfig settings;
    
    public TileEntityUltimateHybridSolar() {
        super(TileEntityUltimateHybridSolar.settings);
    }
}
