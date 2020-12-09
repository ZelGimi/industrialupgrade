package com.denfop.ssp.tiles.panels.overtime;

import com.denfop.ssp.tiles.panels.entity.TileEntitySolarPanel;

public class TileEntitySpectral extends TileEntitySolarPanel {
	public static TileEntitySolarPanel.SolarConfig settings;

	public TileEntitySpectral() {
		super(TileEntitySpectral.settings);
	}
}
