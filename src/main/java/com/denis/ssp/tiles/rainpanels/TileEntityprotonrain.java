
package com.Denfop.ssp.tiles.rainpanels;

import com.Denfop.ssp.tiles.TileEntityMoonPanel;
import com.Denfop.ssp.tiles.TileEntityRainPanel;
import com.Denfop.ssp.tiles.TileEntitySolarPanel;
import com.Denfop.ssp.tiles.TileEntitySolarPanelsun;

public class TileEntityprotonrain extends TileEntityRainPanel
{
 public static TileEntityRainPanel.SolarConfig settings;
 
 public TileEntityprotonrain() {
     super(TileEntityprotonrain.settings);
 }
}
