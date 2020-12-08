package com.denfop.ssp.tiles.rainpanels;

import com.denfop.ssp.tiles.TileEntityMoonPanel;
import com.denfop.ssp.tiles.TileEntityRainPanel;
import com.denfop.ssp.tiles.TileEntitySolarPanel;
import com.denfop.ssp.tiles.TileEntitySolarPanelsun;

public class TileEntitySpectralrain extends TileEntityRainPanel
{
    public static TileEntityRainPanel.SolarConfig settings;
    
    public TileEntitySpectralrain() {
        super(TileEntitySpectralrain.settings);
    }
}
