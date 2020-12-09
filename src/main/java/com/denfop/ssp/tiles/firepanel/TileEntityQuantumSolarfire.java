package com.denfop.ssp.tiles.firepanel;

import com.denfop.ssp.tiles.TileEntityNetherPanel;

public class TileEntityQuantumSolarfire extends TileEntityNetherPanel {
	public static SolarConfig settings;

	public TileEntityQuantumSolarfire() {
		super(TileEntityQuantumSolarfire.settings);
	}
}
