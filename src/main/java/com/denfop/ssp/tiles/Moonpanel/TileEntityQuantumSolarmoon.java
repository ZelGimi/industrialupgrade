package com.denfop.ssp.tiles.Moonpanel;

import com.denfop.ssp.tiles.TileEntityMoonPanel;

public class TileEntityQuantumSolarmoon extends TileEntityMoonPanel {
	public static SolarConfig settings;

	public TileEntityQuantumSolarmoon() {
		super(TileEntityQuantumSolarmoon.settings);
	}
}
