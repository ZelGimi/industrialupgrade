package com.Denfop.ssp.tiles.overtimepanel;

import com.Denfop.ssp.tiles.TileEntitySolarPanel;

public class TileEntityHybridSolar extends TileEntitySolarPanel
{
    public static SolarConfig settings;
    
    public TileEntityHybridSolar() {
        super(TileEntityHybridSolar.settings);
    }
}
