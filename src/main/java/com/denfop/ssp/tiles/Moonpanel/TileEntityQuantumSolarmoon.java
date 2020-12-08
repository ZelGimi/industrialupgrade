package com.denfop.ssp.tiles.Moonpanel;

import com.denfop.ssp.tiles.TileEntityMoonPanel;
import com.denfop.ssp.tiles.TileEntitySolarPanel;
import com.denfop.ssp.tiles.TileEntitySolarPanelsun;

public class TileEntityQuantumSolarmoon extends TileEntityMoonPanel
{
    public static SolarConfig settings;
    
    public TileEntityQuantumSolarmoon() {
        super(TileEntityQuantumSolarmoon.settings);
    }
}
