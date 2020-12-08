package com.denfop.ssp.tiles.endpanel;

import com.denfop.ssp.tiles.TileEntityEnderPanel;
import com.denfop.ssp.tiles.TileEntityMoonPanel;
import com.denfop.ssp.tiles.TileEntitySolarPanel;
import com.denfop.ssp.tiles.TileEntitySolarPanelsun;

public class TileEntitySingularend extends TileEntityEnderPanel
{
    public static TileEntityEnderPanel.SolarConfig settings;
    
    public TileEntitySingularend() {
        super(TileEntitySingularend.settings);
    }
}
