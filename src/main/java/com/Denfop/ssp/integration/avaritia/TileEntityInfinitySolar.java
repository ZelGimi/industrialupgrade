
package com.Denfop.ssp.integration.avaritia;

import com.Denfop.ssp.tiles.TileEntitySolarPanel;

public class TileEntityInfinitySolar extends TileEntitySolarPanel
{
    public static TileEntitySolarPanel.SolarConfig settings;
    
    public TileEntityInfinitySolar() {
        super(TileEntityInfinitySolar.settings);
    }
}
