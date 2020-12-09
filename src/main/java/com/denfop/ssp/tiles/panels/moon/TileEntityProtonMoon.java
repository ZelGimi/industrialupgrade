package com.denfop.ssp.tiles.panels.moon;

import com.denfop.ssp.tiles.panels.entity.TileEntityMoonPanel;

public class TileEntityProtonMoon extends TileEntityMoonPanel {
	public static TileEntityMoonPanel.SolarConfig settings;

	public TileEntityProtonMoon() {
		super(TileEntityProtonMoon.settings);
	}
}
