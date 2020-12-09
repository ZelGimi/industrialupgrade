package com.denfop.ssp.tiles.panels.end;

import com.denfop.ssp.tiles.panels.entity.TileEntityEnderPanel;

public class TileEntityQuantumSolarend extends TileEntityEnderPanel {
	public static SolarConfig settings;

	public TileEntityQuantumSolarend() {
		super(TileEntityQuantumSolarend.settings);
	}
}
