package com.denfop.ssp.tiles.panels.sun;

import com.denfop.ssp.tiles.panels.entity.TileEntitySolarPanelsun;

public class TileEntityPhotonicSun extends TileEntitySolarPanelsun {
	public static TileEntitySolarPanelsun.SolarConfig settings;

	public TileEntityPhotonicSun() {
		super(TileEntityPhotonicSun.settings);
	}


}
