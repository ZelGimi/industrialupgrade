package com.denfop.ssp.tiles.Sunpanel;

import com.denfop.ssp.tiles.TileEntitySolarPanelsun;

public class TileEntitySpectralsun extends TileEntitySolarPanelsun {
	public static TileEntitySolarPanelsun.SolarConfig settings;

	public TileEntitySpectralsun() {
		super(TileEntitySpectralsun.settings);
	}
}
