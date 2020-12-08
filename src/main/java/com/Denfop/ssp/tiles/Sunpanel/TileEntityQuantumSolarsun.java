package com.Denfop.ssp.tiles.Sunpanel;

import com.Denfop.ssp.tiles.TileEntitySolarPanel;
import com.Denfop.ssp.tiles.TileEntitySolarPanelsun;

public class TileEntityQuantumSolarsun extends TileEntitySolarPanelsun
{
    public static SolarConfig settings;
    
    public TileEntityQuantumSolarsun() {
        super(TileEntityQuantumSolarsun.settings);
    }
}
