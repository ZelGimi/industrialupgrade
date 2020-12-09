package com.denfop.ssp.tiles.panels.air;

import com.denfop.ssp.tiles.panels.entity.TileEntityAirPanel;

public class TileEntityHybridSolarair extends TileEntityAirPanel {
	public static SolarConfig settings;

	public TileEntityHybridSolarair() {
		super(TileEntityHybridSolarair.settings);
	}
}
