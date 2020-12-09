package com.denfop.ssp.tiles.panels.earth;

import com.denfop.ssp.tiles.panels.entity.TileEntityEarthPanel;

public class TileEntityAdvancedSolarearth extends TileEntityEarthPanel {
	public static SolarConfig settings;

	public TileEntityAdvancedSolarearth() {
		super(TileEntityAdvancedSolarearth.settings);
	}
}
