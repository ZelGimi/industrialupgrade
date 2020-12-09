package com.denfop.ssp.tiles.panels.sun;

import com.denfop.ssp.tiles.panels.entity.TileEntitySolarPanelsun;

public class TileEntityHybridSolarsun extends TileEntitySolarPanelsun {
	public static SolarConfig settings;

	public TileEntityHybridSolarsun() {
		super(TileEntityHybridSolarsun.settings);
	}
}
