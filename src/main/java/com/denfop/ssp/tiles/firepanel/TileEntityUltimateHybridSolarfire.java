package com.denfop.ssp.tiles.firepanel;

import com.denfop.ssp.tiles.TileEntityMoonPanel;
import com.denfop.ssp.tiles.TileEntityNetherPanel;
import com.denfop.ssp.tiles.TileEntitySolarPanel;
import com.denfop.ssp.tiles.TileEntitySolarPanelsun;

public class TileEntityUltimateHybridSolarfire extends TileEntityNetherPanel
{
    public static SolarConfig settings;
    
    public TileEntityUltimateHybridSolarfire() {
        super(TileEntityUltimateHybridSolarfire.settings);
    }
}
