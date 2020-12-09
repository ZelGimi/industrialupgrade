package com.denfop.ssp.tiles.panels.earth;

import com.denfop.ssp.tiles.panels.entity.TileEntityEarthPanel;

public class TileEntityQuantumSolarearth extends TileEntityEarthPanel {
	public static SolarConfig settings;

	public TileEntityQuantumSolarearth() {
		super(TileEntityQuantumSolarearth.settings);
	}
}
