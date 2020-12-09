package com.denfop.ssp.tiles.panels.sun;

import com.denfop.ssp.tiles.panels.entity.TileEntitySolarPanelsun;

public class TileEntitySpectralsun extends TileEntitySolarPanelsun {
	public static TileEntitySolarPanelsun.SolarConfig settings;

	public TileEntitySpectralsun() {
		super(TileEntitySpectralsun.settings);
	}
}
