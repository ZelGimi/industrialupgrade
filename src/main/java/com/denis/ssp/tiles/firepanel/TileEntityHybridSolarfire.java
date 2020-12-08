package com.Denfop.ssp.tiles.firepanel;

import com.Denfop.ssp.tiles.TileEntityMoonPanel;
import com.Denfop.ssp.tiles.TileEntityNetherPanel;
import com.Denfop.ssp.tiles.TileEntitySolarPanel;
import com.Denfop.ssp.tiles.TileEntitySolarPanelsun;

public class TileEntityHybridSolarfire extends TileEntityNetherPanel
{
    public static SolarConfig settings;
    
    public TileEntityHybridSolarfire() {
        super(TileEntityHybridSolarfire.settings);
    }
}
