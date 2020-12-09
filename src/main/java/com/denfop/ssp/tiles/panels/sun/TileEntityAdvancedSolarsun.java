package com.denfop.ssp.tiles.panels.sun;

import com.denfop.ssp.tiles.panels.entity.TileEntitySolarPanelsun;

public class TileEntityAdvancedSolarsun extends TileEntitySolarPanelsun {
	public static SolarConfig settings;

	public TileEntityAdvancedSolarsun() {
		super(TileEntityAdvancedSolarsun.settings);
	}
}
