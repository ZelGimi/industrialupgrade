package com.denfop.ssp.tiles.panels.earth;

import com.denfop.ssp.tiles.panels.entity.TileEntityEarthPanel;

public class TileEntityHybridSolarearth extends TileEntityEarthPanel {
	public static SolarConfig settings;

	public TileEntityHybridSolarearth() {
		super(TileEntityHybridSolarearth.settings);
	}
}
