package com.denfop.ssp.tiles.endpanel;

import com.denfop.ssp.tiles.TileEntityEnderPanel;

public class TileEntityQuantumSolarend extends TileEntityEnderPanel {
	public static SolarConfig settings;

	public TileEntityQuantumSolarend() {
		super(TileEntityQuantumSolarend.settings);
	}
}
