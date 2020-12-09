package com.denfop.ssp.tiles.panels.end;

import com.denfop.ssp.tiles.panels.entity.TileEntityEnderPanel;

public class TileEntityUltimateHybridSolarend extends TileEntityEnderPanel {
	public static SolarConfig settings;

	public TileEntityUltimateHybridSolarend() {
		super(TileEntityUltimateHybridSolarend.settings);
	}
}
