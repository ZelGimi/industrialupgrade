package com.denfop.ssp.tiles.panels.sun;

import com.denfop.ssp.tiles.panels.entity.TileEntitySolarPanelsun;

public class TileEntitySingularsun extends TileEntitySolarPanelsun {
	public static TileEntitySolarPanelsun.SolarConfig settings;

	public TileEntitySingularsun() {
		super(TileEntitySingularsun.settings);
	}
}
