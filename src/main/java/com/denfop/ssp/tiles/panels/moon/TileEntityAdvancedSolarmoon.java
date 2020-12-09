package com.denfop.ssp.tiles.panels.moon;

import com.denfop.ssp.tiles.panels.entity.TileEntityMoonPanel;

public class TileEntityAdvancedSolarmoon extends TileEntityMoonPanel {
	public static SolarConfig settings;

	public TileEntityAdvancedSolarmoon() {
		super(TileEntityAdvancedSolarmoon.settings);
	}
}
