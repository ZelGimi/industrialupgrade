package com.Denfop.ssp.tiles.overtimepanel;

import com.Denfop.ssp.tiles.TileEntitySolarPanel;

public class TileEntityQuantumSolar extends TileEntitySolarPanel
{
    public static SolarConfig settings;
    
    public TileEntityQuantumSolar() {
        super(TileEntityQuantumSolar.settings);
    }
}
