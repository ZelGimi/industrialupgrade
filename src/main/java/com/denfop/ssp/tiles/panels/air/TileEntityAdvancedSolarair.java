package com.denfop.ssp.tiles.panels.air;

import com.denfop.ssp.tiles.panels.entity.TileEntityAirPanel;

public class TileEntityAdvancedSolarair extends TileEntityAirPanel {
	public static SolarConfig settings;

	public TileEntityAdvancedSolarair() {
		super(TileEntityAdvancedSolarair.settings);
	}
}
