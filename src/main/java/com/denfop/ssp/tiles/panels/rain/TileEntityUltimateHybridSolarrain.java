package com.denfop.ssp.tiles.panels.rain;

import com.denfop.ssp.tiles.panels.entity.TileEntityRainPanel;

public class TileEntityUltimateHybridSolarrain extends TileEntityRainPanel {
	public static SolarConfig settings;

	public TileEntityUltimateHybridSolarrain() {
		super(TileEntityUltimateHybridSolarrain.settings);
	}
}
