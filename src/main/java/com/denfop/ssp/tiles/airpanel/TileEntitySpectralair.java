package com.denfop.ssp.tiles.airpanel;

import com.denfop.ssp.tiles.TileEntityAirPanel;
import com.denfop.ssp.tiles.TileEntityMoonPanel;
import com.denfop.ssp.tiles.TileEntitySolarPanel;
import com.denfop.ssp.tiles.TileEntitySolarPanelsun;

public class TileEntitySpectralair extends TileEntityAirPanel
{
    public static TileEntityAirPanel.SolarConfig settings;
    
    public TileEntitySpectralair() {
        super(TileEntitySpectralair.settings);
    }
}
