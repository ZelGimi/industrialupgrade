package com.denfop.ssp.tiles.panels.overtime;

import com.denfop.ssp.tiles.panels.entity.TileEntitySolarPanel;

public class TileEntitySingular extends TileEntitySolarPanel {
	public static TileEntitySolarPanel.SolarConfig settings;

	public TileEntitySingular() {
		super(TileEntitySingular.settings);
	}
}
