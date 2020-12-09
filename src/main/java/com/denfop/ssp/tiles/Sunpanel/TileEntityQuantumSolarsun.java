package com.denfop.ssp.tiles.Sunpanel;

import com.denfop.ssp.tiles.TileEntitySolarPanelsun;

public class TileEntityQuantumSolarsun extends TileEntitySolarPanelsun {
	public static SolarConfig settings;

	public TileEntityQuantumSolarsun() {
		super(TileEntityQuantumSolarsun.settings);
	}
}
