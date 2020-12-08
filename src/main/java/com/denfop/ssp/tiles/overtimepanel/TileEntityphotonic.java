package com.denfop.ssp.tiles.overtimepanel;

import com.denfop.ssp.tiles.TileEntitySolarPanel;

public class TileEntityphotonic extends TileEntitySolarPanel
{
    public static TileEntitySolarPanel.SolarConfig settings;
    
    public TileEntityphotonic() {
        super(TileEntityphotonic.settings);
    }

	
}
