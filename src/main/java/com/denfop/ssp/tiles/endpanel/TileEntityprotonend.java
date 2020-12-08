
package com.denfop.ssp.tiles.endpanel;

import com.denfop.ssp.tiles.TileEntityEnderPanel;
import com.denfop.ssp.tiles.TileEntityMoonPanel;
import com.denfop.ssp.tiles.TileEntitySolarPanel;
import com.denfop.ssp.tiles.TileEntitySolarPanelsun;

public class TileEntityprotonend extends TileEntityEnderPanel
{
 public static TileEntityEnderPanel.SolarConfig settings;
 
 public TileEntityprotonend() {
     super(TileEntityprotonend.settings);
 }
}
