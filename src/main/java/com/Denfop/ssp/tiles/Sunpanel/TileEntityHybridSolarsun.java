package com.Denfop.ssp.tiles.Sunpanel;

import com.Denfop.ssp.tiles.TileEntitySolarPanel;
import com.Denfop.ssp.tiles.TileEntitySolarPanelsun;

public class TileEntityHybridSolarsun extends TileEntitySolarPanelsun
{
    public static SolarConfig settings;
    
    public TileEntityHybridSolarsun() {
        super(TileEntityHybridSolarsun.settings);
    }
}
