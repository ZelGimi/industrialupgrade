package com.denfop.ssp.tiles.earthpanel;

import com.denfop.ssp.tiles.TileEntityEarthPanel;

public class TileEntityQuantumSolarearth extends TileEntityEarthPanel {
	public static SolarConfig settings;

	public TileEntityQuantumSolarearth() {
		super(TileEntityQuantumSolarearth.settings);
	}
}
