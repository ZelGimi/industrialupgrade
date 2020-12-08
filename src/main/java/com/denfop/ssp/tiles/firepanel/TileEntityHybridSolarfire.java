package com.denfop.ssp.tiles.firepanel;

import com.denfop.ssp.tiles.TileEntityMoonPanel;
import com.denfop.ssp.tiles.TileEntityNetherPanel;
import com.denfop.ssp.tiles.TileEntitySolarPanel;
import com.denfop.ssp.tiles.TileEntitySolarPanelsun;

public class TileEntityHybridSolarfire extends TileEntityNetherPanel
{
    public static SolarConfig settings;
    
    public TileEntityHybridSolarfire() {
        super(TileEntityHybridSolarfire.settings);
    }
}
