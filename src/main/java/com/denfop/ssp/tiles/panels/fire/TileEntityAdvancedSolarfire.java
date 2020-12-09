package com.denfop.ssp.tiles.panels.fire;

import com.denfop.ssp.tiles.panels.entity.TileEntityNetherPanel;

public class TileEntityAdvancedSolarfire extends TileEntityNetherPanel {
	public static SolarConfig settings;

	public TileEntityAdvancedSolarfire() {
		super(TileEntityAdvancedSolarfire.settings);
	}
}
