package com.denfop.ssp.tiles.airpanel;

import com.denfop.ssp.tiles.TileEntityAirPanel;

public class TileEntityQuantumSolarair extends TileEntityAirPanel
{
    public static SolarConfig settings;
    
    public TileEntityQuantumSolarair() {
        super(TileEntityQuantumSolarair.settings);
    }
}
