package com.denfop.ssp.tiles.Moonpanel;

import com.denfop.ssp.tiles.TileEntityMoonPanel;

public class TileEntityprotonmoon extends TileEntityMoonPanel
{
 public static TileEntityMoonPanel.SolarConfig settings;
 
 public TileEntityprotonmoon() {
     super(TileEntityprotonmoon.settings);
 }
}
