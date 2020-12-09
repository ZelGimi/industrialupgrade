package com.denfop.ssp.tiles.airpanel;

import com.denfop.ssp.tiles.TileEntityAirPanel;

public class TileEntityUltimateHybridSolarair extends TileEntityAirPanel {
	public static SolarConfig settings;

	public TileEntityUltimateHybridSolarair() {
		super(TileEntityUltimateHybridSolarair.settings);
	}
}
