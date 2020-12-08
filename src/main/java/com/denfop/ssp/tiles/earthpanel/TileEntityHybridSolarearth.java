package com.denfop.ssp.tiles.earthpanel;

import com.denfop.ssp.tiles.TileEntityEarthPanel;
import com.denfop.ssp.tiles.TileEntityMoonPanel;
import com.denfop.ssp.tiles.TileEntitySolarPanel;
import com.denfop.ssp.tiles.TileEntitySolarPanelsun;

public class TileEntityHybridSolarearth extends TileEntityEarthPanel
{
    public static SolarConfig settings;
    
    public TileEntityHybridSolarearth() {
        super(TileEntityHybridSolarearth.settings);
    }
}
