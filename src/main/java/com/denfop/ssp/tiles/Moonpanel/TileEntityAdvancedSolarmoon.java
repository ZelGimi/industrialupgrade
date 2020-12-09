package com.denfop.ssp.tiles.Moonpanel;

import com.denfop.ssp.tiles.TileEntityMoonPanel;

public class TileEntityAdvancedSolarmoon extends TileEntityMoonPanel {
	public static SolarConfig settings;

	public TileEntityAdvancedSolarmoon() {
		super(TileEntityAdvancedSolarmoon.settings);
	}
}
