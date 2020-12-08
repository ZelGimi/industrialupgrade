package com.denfop.ssp.tiles.endpanel;

import com.denfop.ssp.tiles.TileEntityEnderPanel;
import com.denfop.ssp.tiles.TileEntityMoonPanel;
import com.denfop.ssp.tiles.TileEntitySolarPanel;
import com.denfop.ssp.tiles.TileEntitySolarPanelsun;

public class TileEntityneutronend extends TileEntityEnderPanel
{
    public static SolarConfig settings;
    
    public TileEntityneutronend() {
        super(TileEntityneutronend.settings);
    }
}
