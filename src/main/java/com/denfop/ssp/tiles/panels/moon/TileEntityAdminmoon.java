package com.denfop.ssp.tiles.panels.moon;

import com.denfop.ssp.tiles.panels.entity.TileEntityMoonPanel;

public class TileEntityAdminmoon extends TileEntityMoonPanel {
	public static SolarConfig settings;

	public TileEntityAdminmoon() {
		super(TileEntityAdminmoon.settings);
	}


}
