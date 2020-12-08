package com.Denfop.ssp.tiles.Sunpanel;

import com.Denfop.ssp.tiles.TileEntitySolarPanel;
import com.Denfop.ssp.tiles.TileEntitySolarPanelsun;

public class TileEntitySpectralsun extends TileEntitySolarPanelsun
{
    public static TileEntitySolarPanelsun.SolarConfig settings;
    
    public TileEntitySpectralsun() {
        super(TileEntitySpectralsun.settings);
    }
}
