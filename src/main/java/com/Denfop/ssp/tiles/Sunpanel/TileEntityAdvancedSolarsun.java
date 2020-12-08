package com.Denfop.ssp.tiles.Sunpanel;

import com.Denfop.ssp.tiles.TileEntitySolarPanel;
import com.Denfop.ssp.tiles.TileEntitySolarPanelsun;

public class TileEntityAdvancedSolarsun extends TileEntitySolarPanelsun
{
    public static SolarConfig settings;
    
    public TileEntityAdvancedSolarsun() {
        super(TileEntityAdvancedSolarsun.settings);
    }
}
