package com.denfop.ssp.tiles.rainpanels;

import com.denfop.ssp.tiles.TileEntityRainPanel;

public class TileEntityQuantumSolarrain extends TileEntityRainPanel {
	public static SolarConfig settings;

	public TileEntityQuantumSolarrain() {
		super(TileEntityQuantumSolarrain.settings);
	}
}
