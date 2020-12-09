package com.denfop.ssp.tiles.panels.air;

import com.denfop.ssp.tiles.panels.entity.TileEntityAirPanel;

public class TileEntityUltimateHybridSolarair extends TileEntityAirPanel {
	public static SolarConfig settings;

	public TileEntityUltimateHybridSolarair() {
		super(TileEntityUltimateHybridSolarair.settings);
	}
}
