package com.denfop.ssp.tiles.firepanel;

import com.denfop.ssp.tiles.TileEntityMoonPanel;
import com.denfop.ssp.tiles.TileEntityNetherPanel;
import com.denfop.ssp.tiles.TileEntitySolarPanel;
import com.denfop.ssp.tiles.TileEntitySolarPanelsun;

public class TileEntitySingularfire extends TileEntityNetherPanel
{
    public static TileEntityNetherPanel.SolarConfig settings;
    
    public TileEntitySingularfire() {
        super(TileEntitySingularfire.settings);
    }
}
