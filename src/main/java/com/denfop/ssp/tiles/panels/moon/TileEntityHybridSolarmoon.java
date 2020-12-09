package com.denfop.ssp.tiles.panels.moon;

import com.denfop.ssp.tiles.panels.entity.TileEntityMoonPanel;

public class TileEntityHybridSolarmoon extends TileEntityMoonPanel {
	public static SolarConfig settings;

	public TileEntityHybridSolarmoon() {
		super(TileEntityHybridSolarmoon.settings);
	}
}
