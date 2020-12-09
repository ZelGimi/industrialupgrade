package com.denfop.ssp.tiles.panels.moon;

import com.denfop.ssp.tiles.panels.entity.TileEntityMoonPanel;

public class TileEntityQuantumSolarmoon extends TileEntityMoonPanel {
	public static SolarConfig settings;

	public TileEntityQuantumSolarmoon() {
		super(TileEntityQuantumSolarmoon.settings);
	}
}
