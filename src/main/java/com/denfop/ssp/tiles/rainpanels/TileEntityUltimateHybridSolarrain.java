package com.denfop.ssp.tiles.rainpanels;

import com.denfop.ssp.tiles.TileEntityRainPanel;

public class TileEntityUltimateHybridSolarrain extends TileEntityRainPanel {
	public static SolarConfig settings;

	public TileEntityUltimateHybridSolarrain() {
		super(TileEntityUltimateHybridSolarrain.settings);
	}
}
