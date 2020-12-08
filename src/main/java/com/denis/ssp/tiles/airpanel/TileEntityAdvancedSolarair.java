package com.Denfop.ssp.tiles.airpanel;

import com.Denfop.ssp.tiles.TileEntityAirPanel;
import com.Denfop.ssp.tiles.TileEntityMoonPanel;
import com.Denfop.ssp.tiles.TileEntitySolarPanel;
import com.Denfop.ssp.tiles.TileEntitySolarPanelsun;

public class TileEntityAdvancedSolarair extends TileEntityAirPanel
{
    public static SolarConfig settings;
    
    public TileEntityAdvancedSolarair() {
        super(TileEntityAdvancedSolarair.settings);
    }
}
