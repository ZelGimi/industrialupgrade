package com.denfop.ssp.tiles.panels.overtime;

import com.denfop.ssp.tiles.panels.entity.TileEntitySolarPanel;

public class TileEntityProton extends TileEntitySolarPanel {
	public static TileEntitySolarPanel.SolarConfig settings;

	public TileEntityProton() {
		super(TileEntityProton.settings);
	}
}
