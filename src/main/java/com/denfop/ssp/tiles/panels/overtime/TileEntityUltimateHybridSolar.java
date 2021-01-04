package com.denfop.ssp.tiles.panels.overtime;

import com.denfop.ssp.tiles.panels.entity.TileEntitySolarPanel;

public class TileEntityUltimateHybridSolar extends TileEntitySolarPanel {
	public static SolarConfig settings;

	public TileEntityUltimateHybridSolar() {
		super(TileEntityUltimateHybridSolar.settings);
	}
}
