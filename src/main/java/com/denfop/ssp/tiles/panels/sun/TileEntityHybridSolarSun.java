package com.denfop.ssp.tiles.panels.sun;

import com.denfop.ssp.tiles.panels.entity.TileEntitySunPanel;

public class TileEntityHybridSolarSun extends TileEntitySunPanel {
	public static SolarConfig settings;

	public TileEntityHybridSolarSun() {
		super(TileEntityHybridSolarSun.settings);
	}
}
