package com.Denfop.ssp.tiles.rainpanels;

import com.Denfop.ssp.tiles.TileEntityMoonPanel;
import com.Denfop.ssp.tiles.TileEntityRainPanel;
import com.Denfop.ssp.tiles.TileEntitySolarPanel;
import com.Denfop.ssp.tiles.TileEntitySolarPanelsun;

public class TileEntityHybridSolarrain extends TileEntityRainPanel
{
    public static SolarConfig settings;
    
    public TileEntityHybridSolarrain() {
        super(TileEntityHybridSolarrain.settings);
    }
}
