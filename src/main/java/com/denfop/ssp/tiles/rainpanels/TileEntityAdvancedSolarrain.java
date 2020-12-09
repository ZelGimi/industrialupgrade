package com.denfop.ssp.tiles.rainpanels;

import com.denfop.ssp.tiles.TileEntityRainPanel;

public class TileEntityAdvancedSolarrain extends TileEntityRainPanel {
	public static SolarConfig settings;

	public TileEntityAdvancedSolarrain() {
		super(TileEntityAdvancedSolarrain.settings);
	}
}
