package com.denfop.ssp.tiles.panels.overtime;

import com.denfop.ssp.tiles.panels.entity.TileEntitySolarPanel;

public class TileEntityQuantumSolar extends TileEntitySolarPanel {
	public static SolarConfig settings;

	public TileEntityQuantumSolar() {
		super(TileEntityQuantumSolar.settings);
	}
}
