package com.Denfop.ssp.tiles.rainpanels;

import com.Denfop.ssp.tiles.TileEntityMoonPanel;
import com.Denfop.ssp.tiles.TileEntityRainPanel;
import com.Denfop.ssp.tiles.TileEntitySolarPanel;
import com.Denfop.ssp.tiles.TileEntitySolarPanelsun;

public class TileEntitySpectralrain extends TileEntityRainPanel
{
    public static TileEntityRainPanel.SolarConfig settings;
    
    public TileEntitySpectralrain() {
        super(TileEntitySpectralrain.settings);
    }
}
