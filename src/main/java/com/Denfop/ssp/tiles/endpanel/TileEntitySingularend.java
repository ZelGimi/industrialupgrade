package com.Denfop.ssp.tiles.endpanel;

import com.Denfop.ssp.tiles.TileEntityEnderPanel;
import com.Denfop.ssp.tiles.TileEntityMoonPanel;
import com.Denfop.ssp.tiles.TileEntitySolarPanel;
import com.Denfop.ssp.tiles.TileEntitySolarPanelsun;

public class TileEntitySingularend extends TileEntityEnderPanel
{
    public static TileEntityEnderPanel.SolarConfig settings;
    
    public TileEntitySingularend() {
        super(TileEntitySingularend.settings);
    }
}
