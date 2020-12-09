package com.denfop.ssp.tiles.panels.sun;

import com.denfop.ssp.tiles.panels.entity.TileEntitySolarPanelsun;

public class TileEntityProtonSun extends TileEntitySolarPanelsun {
	public static TileEntitySolarPanelsun.SolarConfig settings;

	public TileEntityProtonSun() {
		super(TileEntityProtonSun.settings);
	}
}
