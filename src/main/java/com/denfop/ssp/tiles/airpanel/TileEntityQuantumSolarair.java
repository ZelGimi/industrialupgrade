package com.denfop.ssp.tiles.airpanel;

import com.denfop.ssp.tiles.TileEntityAirPanel;
import com.denfop.ssp.tiles.TileEntityMoonPanel;
import com.denfop.ssp.tiles.TileEntitySolarPanel;
import com.denfop.ssp.tiles.TileEntitySolarPanelsun;

public class TileEntityQuantumSolarair extends TileEntityAirPanel
{
    public static SolarConfig settings;
    
    public TileEntityQuantumSolarair() {
        super(TileEntityQuantumSolarair.settings);
    }
}
