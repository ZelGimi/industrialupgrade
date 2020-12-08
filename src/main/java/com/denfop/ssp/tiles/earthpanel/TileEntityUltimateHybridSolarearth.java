package com.denfop.ssp.tiles.earthpanel;

import com.denfop.ssp.tiles.TileEntityEarthPanel;
import com.denfop.ssp.tiles.TileEntityMoonPanel;
import com.denfop.ssp.tiles.TileEntitySolarPanel;
import com.denfop.ssp.tiles.TileEntitySolarPanelsun;

public class TileEntityUltimateHybridSolarearth extends TileEntityEarthPanel
{
    public static SolarConfig settings;
    
    public TileEntityUltimateHybridSolarearth() {
        super(TileEntityUltimateHybridSolarearth.settings);
    }
}
