package com.denfop.ssp.tiles.airpanel;

import com.denfop.ssp.tiles.TileEntityAirPanel;

public class TileEntitySpectralair extends TileEntityAirPanel
{
    public static TileEntityAirPanel.SolarConfig settings;
    
    public TileEntitySpectralair() {
        super(TileEntitySpectralair.settings);
    }
}
