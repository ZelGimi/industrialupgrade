
package com.Denfop.ssp.tiles.overtimepanel;

import com.Denfop.ssp.tiles.TileEntitySolarPanel;

public class TileEntityproton extends TileEntitySolarPanel
{
 public static TileEntitySolarPanel.SolarConfig settings;
 
 public TileEntityproton() {
     super(TileEntityproton.settings);
 }
}
