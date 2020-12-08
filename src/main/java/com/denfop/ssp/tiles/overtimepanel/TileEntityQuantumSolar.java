package com.denfop.ssp.tiles.overtimepanel;

import com.denfop.ssp.tiles.TileEntitySolarPanel;

public class TileEntityQuantumSolar extends TileEntitySolarPanel
{
    public static SolarConfig settings;
    
    public TileEntityQuantumSolar() {
        super(TileEntityQuantumSolar.settings);
    }
}
