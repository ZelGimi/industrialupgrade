package com.denfop.ssp.tiles.panels.earth;

import com.denfop.ssp.tiles.panels.entity.TileEntityEarthPanel;

public class TileEntityNeutronEarth extends TileEntityEarthPanel {
	public static SolarConfig settings;

	public TileEntityNeutronEarth() {
		super(TileEntityNeutronEarth.settings);
	}
}
