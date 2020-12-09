package com.denfop.ssp.tiles.panels.moon;

import com.denfop.ssp.tiles.panels.entity.TileEntityMoonPanel;

public class TileEntityNeutronMoon extends TileEntityMoonPanel {
	public static SolarConfig settings;

	public TileEntityNeutronMoon() {
		super(TileEntityNeutronMoon.settings);
	}
}
