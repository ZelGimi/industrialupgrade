package com.denfop.ssp.tiles.panels.rain;

import com.denfop.ssp.tiles.panels.entity.TileEntityRainPanel;

public class TileEntityQuantumSolarrain extends TileEntityRainPanel {
	public static SolarConfig settings;

	public TileEntityQuantumSolarrain() {
		super(TileEntityQuantumSolarrain.settings);
	}
}
