package com.Denfop.ssp.tiles.airpanel;

import com.Denfop.ssp.tiles.TileEntityAirPanel;
import com.Denfop.ssp.tiles.TileEntityMoonPanel;
import com.Denfop.ssp.tiles.TileEntitySolarPanel;
import com.Denfop.ssp.tiles.TileEntitySolarPanelsun;

public class TileEntitySingularair extends TileEntityAirPanel
{
    public static TileEntityAirPanel.SolarConfig settings;
    
    public TileEntitySingularair() {
        super(TileEntitySingularair.settings);
    }
}
