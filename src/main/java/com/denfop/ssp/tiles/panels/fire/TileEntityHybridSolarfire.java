package com.denfop.ssp.tiles.panels.fire;

import com.denfop.ssp.tiles.panels.entity.TileEntityNetherPanel;

public class TileEntityHybridSolarfire extends TileEntityNetherPanel {
	public static SolarConfig settings;

	public TileEntityHybridSolarfire() {
		super(TileEntityHybridSolarfire.settings);
	}
}
