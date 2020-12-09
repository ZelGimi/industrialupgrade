package com.denfop.ssp.tiles.panels.moon;

import com.denfop.ssp.tiles.panels.entity.TileEntityMoonPanel;

public class TileEntityPhotonicMoon extends TileEntityMoonPanel {
	public static SolarConfig settings;

	public TileEntityPhotonicMoon() {
		super(TileEntityPhotonicMoon.settings);
	}


}
