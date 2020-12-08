// 
// Decompiled by Procyon v0.5.36
// 

package com.Denfop.ssp.tiles.earthpanel;

import com.Denfop.ssp.tiles.TileEntityEarthPanel;
import com.Denfop.ssp.tiles.TileEntityMoonPanel;
import com.Denfop.ssp.tiles.TileEntitySolarPanel;
import com.Denfop.ssp.tiles.TileEntitySolarPanelsun;

public class TileEntityHybridSolarearth extends TileEntityEarthPanel
{
    public static SolarConfig settings;
    
    public TileEntityHybridSolarearth() {
        super(TileEntityHybridSolarearth.settings);
    }
}
