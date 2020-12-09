package com.denfop.ssp.integration.botania;

import com.denfop.ssp.tiles.TileEntitySolarPanel;

public class TileEntityElementiumSolar extends TileEntitySolarPanel {
	public static TileEntitySolarPanel.SolarConfig settings;

	public TileEntityElementiumSolar() {
		super(TileEntityElementiumSolar.settings);
	}
}
