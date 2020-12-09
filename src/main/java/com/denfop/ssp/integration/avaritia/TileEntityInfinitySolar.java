package com.denfop.ssp.integration.avaritia;

import com.denfop.ssp.tiles.panels.entity.TileEntitySolarPanel;

public class TileEntityInfinitySolar extends TileEntitySolarPanel {
	public static TileEntitySolarPanel.SolarConfig settings;

	public TileEntityInfinitySolar() {
		super(TileEntityInfinitySolar.settings);
	}
}
