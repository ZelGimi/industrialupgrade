package com.denfop.ssp.tiles.panels.sun;

import com.denfop.ssp.tiles.panels.entity.TileEntitySunPanel;

public class TileEntitySingularSun extends TileEntitySunPanel {
	public static TileEntitySunPanel.SolarConfig settings;

	public TileEntitySingularSun() {
		super(TileEntitySingularSun.settings);
	}
}
