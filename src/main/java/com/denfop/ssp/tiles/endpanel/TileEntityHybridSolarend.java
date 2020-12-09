package com.denfop.ssp.tiles.endpanel;

import com.denfop.ssp.tiles.TileEntityEnderPanel;

public class TileEntityHybridSolarend extends TileEntityEnderPanel {
	public static SolarConfig settings;

	public TileEntityHybridSolarend() {
		super(TileEntityHybridSolarend.settings);
	}
}
