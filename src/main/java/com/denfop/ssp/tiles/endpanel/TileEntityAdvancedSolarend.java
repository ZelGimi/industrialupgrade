package com.denfop.ssp.tiles.endpanel;

import com.denfop.ssp.tiles.TileEntityEnderPanel;

public class TileEntityAdvancedSolarend extends TileEntityEnderPanel {
	public static SolarConfig settings;

	public TileEntityAdvancedSolarend() {
		super(TileEntityAdvancedSolarend.settings);
	}
}
