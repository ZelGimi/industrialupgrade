package com.denfop.ssp.tiles.endpanel;

import com.denfop.ssp.tiles.TileEntityEnderPanel;
import com.denfop.ssp.tiles.TileEntityMoonPanel;
import com.denfop.ssp.tiles.TileEntitySolarPanel;
import com.denfop.ssp.tiles.TileEntitySolarPanelsun;

public class TileEntitySpectralend extends TileEntityEnderPanel
{
    public static TileEntityEnderPanel.SolarConfig settings;
    
    public TileEntitySpectralend() {
        super(TileEntitySpectralend.settings);
    }
}
