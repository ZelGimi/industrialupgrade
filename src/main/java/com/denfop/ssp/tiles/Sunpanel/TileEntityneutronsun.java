package com.denfop.ssp.tiles.Sunpanel;

import com.denfop.ssp.tiles.TileEntitySolarPanelsun;

public class TileEntityneutronsun extends TileEntitySolarPanelsun
{
    public static TileEntitySolarPanelsun.SolarConfig settings;
    
    public TileEntityneutronsun() {
        super(TileEntityneutronsun.settings);
    }
}
