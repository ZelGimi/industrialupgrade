package com.denfop.ssp.tiles.panels.air;

import com.denfop.ssp.tiles.panels.entity.TileEntityAirPanel;

public class TileEntityQuantumSolarair extends TileEntityAirPanel {
	public static SolarConfig settings;

	public TileEntityQuantumSolarair() {
		super(TileEntityQuantumSolarair.settings);
	}
}
