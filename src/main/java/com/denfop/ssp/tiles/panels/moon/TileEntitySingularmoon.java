package com.denfop.ssp.tiles.panels.moon;

import com.denfop.ssp.tiles.panels.entity.TileEntityMoonPanel;

public class TileEntitySingularmoon extends TileEntityMoonPanel {
	public static TileEntityMoonPanel.SolarConfig settings;

	public TileEntitySingularmoon() {
		super(TileEntitySingularmoon.settings);
	}
}
