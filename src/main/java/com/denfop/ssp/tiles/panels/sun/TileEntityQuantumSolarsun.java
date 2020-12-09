package com.denfop.ssp.tiles.panels.sun;

import com.denfop.ssp.tiles.panels.entity.TileEntitySolarPanelsun;

public class TileEntityQuantumSolarsun extends TileEntitySolarPanelsun {
	public static SolarConfig settings;

	public TileEntityQuantumSolarsun() {
		super(TileEntityQuantumSolarsun.settings);
	}
}
