package com.denfop.ssp.tiles.overtimepanel;

import com.denfop.ssp.tiles.TileEntitySolarPanel;

public class TileEntitySingular extends TileEntitySolarPanel
{
    public static TileEntitySolarPanel.SolarConfig settings;
    
    public TileEntitySingular() {
        super(TileEntitySingular.settings);
    }
}
