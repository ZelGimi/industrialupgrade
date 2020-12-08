package com.Denfop.ssp.tiles.overtimepanel;

import com.Denfop.ssp.tiles.TileEntitySolarPanel;

public class TileEntityAdvancedSolar extends TileEntitySolarPanel
{
    public static SolarConfig settings;
    
    public TileEntityAdvancedSolar() {
        super(TileEntityAdvancedSolar.settings);
    }
}
