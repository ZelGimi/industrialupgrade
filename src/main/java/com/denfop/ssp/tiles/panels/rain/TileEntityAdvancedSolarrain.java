package com.denfop.ssp.tiles.panels.rain;

import com.denfop.ssp.tiles.panels.entity.TileEntityRainPanel;

public class TileEntityAdvancedSolarrain extends TileEntityRainPanel {
	public static SolarConfig settings;

	public TileEntityAdvancedSolarrain() {
		super(TileEntityAdvancedSolarrain.settings);
	}
}
