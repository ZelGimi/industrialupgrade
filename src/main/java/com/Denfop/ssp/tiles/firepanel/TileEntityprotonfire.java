
package com.Denfop.ssp.tiles.firepanel;

import com.Denfop.ssp.tiles.TileEntityMoonPanel;
import com.Denfop.ssp.tiles.TileEntityNetherPanel;
import com.Denfop.ssp.tiles.TileEntitySolarPanel;
import com.Denfop.ssp.tiles.TileEntitySolarPanelsun;

public class TileEntityprotonfire extends TileEntityNetherPanel
{
 public static TileEntityNetherPanel.SolarConfig settings;
 
 public TileEntityprotonfire() {
     super(TileEntityprotonfire.settings);
 }
}
