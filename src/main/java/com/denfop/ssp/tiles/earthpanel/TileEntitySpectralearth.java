package com.denfop.ssp.tiles.earthpanel;

import com.denfop.ssp.tiles.TileEntityEarthPanel;
import com.denfop.ssp.tiles.TileEntityMoonPanel;
import com.denfop.ssp.tiles.TileEntitySolarPanel;
import com.denfop.ssp.tiles.TileEntitySolarPanelsun;

public class TileEntitySpectralearth extends TileEntityEarthPanel
{
    public static TileEntityEarthPanel.SolarConfig settings;
    
    public TileEntitySpectralearth() {
        super(TileEntitySpectralearth.settings);
    }
}
