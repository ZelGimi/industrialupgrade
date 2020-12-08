package com.Denfop.ssp.tiles.Sunpanel;

import com.Denfop.ssp.tiles.TileEntitySolarPanel;
import com.Denfop.ssp.tiles.TileEntitySolarPanelsun;

public class TileEntitySingularsun extends TileEntitySolarPanelsun
{
    public static TileEntitySolarPanelsun.SolarConfig settings;
    
    public TileEntitySingularsun() {
        super(TileEntitySingularsun.settings);
    }
}
