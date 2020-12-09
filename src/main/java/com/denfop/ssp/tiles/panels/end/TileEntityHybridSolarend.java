package com.denfop.ssp.tiles.panels.end;

import com.denfop.ssp.tiles.panels.entity.TileEntityEnderPanel;

public class TileEntityHybridSolarend extends TileEntityEnderPanel {
	public static SolarConfig settings;

	public TileEntityHybridSolarend() {
		super(TileEntityHybridSolarend.settings);
	}
}
