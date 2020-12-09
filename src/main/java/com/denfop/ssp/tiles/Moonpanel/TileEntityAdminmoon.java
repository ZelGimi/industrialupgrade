package com.denfop.ssp.tiles.Moonpanel;

import com.denfop.ssp.tiles.TileEntityMoonPanel;

public class TileEntityAdminmoon extends TileEntityMoonPanel {
	public static SolarConfig settings;

	public TileEntityAdminmoon() {
		super(TileEntityAdminmoon.settings);
	}


}
