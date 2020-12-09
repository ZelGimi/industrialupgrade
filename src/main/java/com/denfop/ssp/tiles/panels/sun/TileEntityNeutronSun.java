package com.denfop.ssp.tiles.panels.sun;

import com.denfop.ssp.tiles.panels.entity.TileEntitySolarPanelsun;

public class TileEntityNeutronSun extends TileEntitySolarPanelsun {
	public static TileEntitySolarPanelsun.SolarConfig settings;

	public TileEntityNeutronSun() {
		super(TileEntityNeutronSun.settings);
	}
}
