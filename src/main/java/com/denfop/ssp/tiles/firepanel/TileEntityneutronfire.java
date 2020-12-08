package com.denfop.ssp.tiles.firepanel;

import com.denfop.ssp.tiles.TileEntityMoonPanel;
import com.denfop.ssp.tiles.TileEntityNetherPanel;
import com.denfop.ssp.tiles.TileEntitySolarPanel;
import com.denfop.ssp.tiles.TileEntitySolarPanelsun;

public class TileEntityneutronfire extends TileEntityNetherPanel
{
    public static SolarConfig settings;
    
    public TileEntityneutronfire() {
        super(TileEntityneutronfire.settings);
    }
}
