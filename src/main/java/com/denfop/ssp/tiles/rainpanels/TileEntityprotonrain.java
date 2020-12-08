package com.denfop.ssp.tiles.rainpanels;

import com.denfop.ssp.tiles.TileEntityRainPanel;

public class TileEntityprotonrain extends TileEntityRainPanel
{
 public static TileEntityRainPanel.SolarConfig settings;
 
 public TileEntityprotonrain() {
     super(TileEntityprotonrain.settings);
 }
}
