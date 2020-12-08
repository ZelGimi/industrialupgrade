package com.Denfop.ssp.tiles.overtimepanel;

import com.Denfop.ssp.tiles.TileEntitySolarPanel;

public class TileEntitySingular extends TileEntitySolarPanel
{
    public static TileEntitySolarPanel.SolarConfig settings;
    
    public TileEntitySingular() {
        super(TileEntitySingular.settings);
    }
}
