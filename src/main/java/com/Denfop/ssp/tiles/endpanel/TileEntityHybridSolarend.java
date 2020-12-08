// 
// Decompiled by Procyon v0.5.36
// 

package com.Denfop.ssp.tiles.endpanel;

import com.Denfop.ssp.tiles.TileEntityEnderPanel;
import com.Denfop.ssp.tiles.TileEntityMoonPanel;
import com.Denfop.ssp.tiles.TileEntitySolarPanel;
import com.Denfop.ssp.tiles.TileEntitySolarPanelsun;

public class TileEntityHybridSolarend extends TileEntityEnderPanel
{
    public static SolarConfig settings;
    
    public TileEntityHybridSolarend() {
        super(TileEntityHybridSolarend.settings);
    }
}
