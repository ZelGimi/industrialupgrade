package com.denfop.ssp.tiles.endpanel;

import com.denfop.ssp.tiles.TileEntityEnderPanel;
import com.denfop.ssp.tiles.TileEntityMoonPanel;
import com.denfop.ssp.tiles.TileEntitySolarPanel;
import com.denfop.ssp.tiles.TileEntitySolarPanelsun;

public class TileEntityHybridSolarend extends TileEntityEnderPanel
{
    public static SolarConfig settings;
    
    public TileEntityHybridSolarend() {
        super(TileEntityHybridSolarend.settings);
    }
}
