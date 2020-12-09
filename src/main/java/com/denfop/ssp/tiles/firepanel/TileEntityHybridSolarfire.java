package com.denfop.ssp.tiles.firepanel;

import com.denfop.ssp.tiles.TileEntityNetherPanel;

public class TileEntityHybridSolarfire extends TileEntityNetherPanel {
	public static SolarConfig settings;

	public TileEntityHybridSolarfire() {
		super(TileEntityHybridSolarfire.settings);
	}
}
