package com.denfop.ssp.tiles.earthpanel;

import com.denfop.ssp.tiles.TileEntityEarthPanel;

public class TileEntityprotonearth extends TileEntityEarthPanel
{
 public static TileEntityEarthPanel.SolarConfig settings;
 
 public TileEntityprotonearth() {
     super(TileEntityprotonearth.settings);
 }
}
