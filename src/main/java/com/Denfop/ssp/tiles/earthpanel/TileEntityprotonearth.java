
package com.Denfop.ssp.tiles.earthpanel;

import com.Denfop.ssp.tiles.TileEntityEarthPanel;
import com.Denfop.ssp.tiles.TileEntityMoonPanel;
import com.Denfop.ssp.tiles.TileEntitySolarPanel;
import com.Denfop.ssp.tiles.TileEntitySolarPanelsun;

public class TileEntityprotonearth extends TileEntityEarthPanel
{
 public static TileEntityEarthPanel.SolarConfig settings;
 
 public TileEntityprotonearth() {
     super(TileEntityprotonearth.settings);
 }
}
