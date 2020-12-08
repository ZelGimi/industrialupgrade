
package com.Denfop.ssp.tiles.Moonpanel;

import com.Denfop.ssp.tiles.TileEntityMoonPanel;
import com.Denfop.ssp.tiles.TileEntitySolarPanel;
import com.Denfop.ssp.tiles.TileEntitySolarPanelsun;

public class TileEntityprotonmoon extends TileEntityMoonPanel
{
 public static TileEntityMoonPanel.SolarConfig settings;
 
 public TileEntityprotonmoon() {
     super(TileEntityprotonmoon.settings);
 }
}
