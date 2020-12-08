
package com.Denfop.ssp.integration.avaritia;

import com.Denfop.ssp.tiles.TileEntitySolarPanel;

public class TileEntityNeutroniumSolar extends TileEntitySolarPanel
{
    public static TileEntitySolarPanel.SolarConfig settings;
    
    public TileEntityNeutroniumSolar() {
        super(TileEntityNeutroniumSolar.settings);
    }
}
