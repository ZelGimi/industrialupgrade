package com.denfop.ssp.tiles.panels.end;

import com.denfop.ssp.tiles.panels.entity.TileEntityEnderPanel;

public class TileEntityAdvancedSolarend extends TileEntityEnderPanel {
	public static SolarConfig settings;

	public TileEntityAdvancedSolarend() {
		super(TileEntityAdvancedSolarend.settings);
	}
}
