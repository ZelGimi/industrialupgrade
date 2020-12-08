package com.denfop.ssp.integration.botania;

import com.denfop.ssp.tiles.TileEntitySolarPanel;

public class TileEntityTerrasteelSolar extends TileEntitySolarPanel
{
    public static TileEntitySolarPanel.SolarConfig settings;
    
    public TileEntityTerrasteelSolar() {
        super(TileEntityTerrasteelSolar.settings);
    }
}
