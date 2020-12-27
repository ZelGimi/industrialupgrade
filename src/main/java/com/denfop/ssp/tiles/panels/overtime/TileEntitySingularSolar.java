package com.denfop.ssp.tiles.panels.overtime;

import com.denfop.ssp.tiles.panels.entity.TileEntitySolarPanel;

public class TileEntitySingularSolar extends TileEntitySolarPanel {
	public static TileEntitySolarPanel.SolarConfig settings;

	public TileEntitySingularSolar() {
		super(TileEntitySingularSolar.settings);
	}
}
