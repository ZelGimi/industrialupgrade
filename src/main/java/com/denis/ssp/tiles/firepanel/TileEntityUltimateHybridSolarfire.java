package com.Denfop.ssp.tiles.firepanel;

import com.Denfop.ssp.tiles.TileEntityMoonPanel;
import com.Denfop.ssp.tiles.TileEntityNetherPanel;
import com.Denfop.ssp.tiles.TileEntitySolarPanel;
import com.Denfop.ssp.tiles.TileEntitySolarPanelsun;

public class TileEntityUltimateHybridSolarfire extends TileEntityNetherPanel
{
    public static SolarConfig settings;
    
    public TileEntityUltimateHybridSolarfire() {
        super(TileEntityUltimateHybridSolarfire.settings);
    }
}
