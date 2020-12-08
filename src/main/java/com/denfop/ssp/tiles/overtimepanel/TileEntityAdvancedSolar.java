package com.denfop.ssp.tiles.overtimepanel;

import com.denfop.ssp.tiles.TileEntitySolarPanel;

public class TileEntityAdvancedSolar extends TileEntitySolarPanel
{
    public static SolarConfig settings;
    
    public TileEntityAdvancedSolar() {
        super(TileEntityAdvancedSolar.settings);
    }
}
