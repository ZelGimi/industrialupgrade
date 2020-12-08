// 
// Decompiled by Procyon v0.5.36
// 

package com.Denfop.ssp.tiles.Moonpanel;

import com.Denfop.ssp.tiles.TileEntityMoonPanel;
import com.Denfop.ssp.tiles.TileEntitySolarPanel;
import com.Denfop.ssp.tiles.TileEntitySolarPanelsun;

public class TileEntitySingularmoon extends TileEntityMoonPanel
{
    public static TileEntityMoonPanel.SolarConfig settings;
    
    public TileEntitySingularmoon() {
        super(TileEntitySingularmoon.settings);
    }
}
