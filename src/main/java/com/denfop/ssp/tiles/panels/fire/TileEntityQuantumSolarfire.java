package com.denfop.ssp.tiles.panels.fire;

import com.denfop.ssp.tiles.panels.entity.TileEntityNetherPanel;

public class TileEntityQuantumSolarfire extends TileEntityNetherPanel {
	public static SolarConfig settings;

	public TileEntityQuantumSolarfire() {
		super(TileEntityQuantumSolarfire.settings);
	}
}
