package com.denfop.ssp.tiles.Sunpanel;

import com.denfop.ssp.tiles.TileEntitySolarPanel;
import com.denfop.ssp.tiles.TileEntitySolarPanelsun;

public class TileEntitySingularsun extends TileEntitySolarPanelsun
{
    public static TileEntitySolarPanelsun.SolarConfig settings;
    
    public TileEntitySingularsun() {
        super(TileEntitySingularsun.settings);
    }
}
