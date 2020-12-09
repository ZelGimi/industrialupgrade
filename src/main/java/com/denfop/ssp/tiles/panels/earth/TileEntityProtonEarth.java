package com.denfop.ssp.tiles.panels.earth;

import com.denfop.ssp.tiles.panels.entity.TileEntityEarthPanel;

public class TileEntityProtonEarth extends TileEntityEarthPanel {
	public static TileEntityEarthPanel.SolarConfig settings;

	public TileEntityProtonEarth() {
		super(TileEntityProtonEarth.settings);
	}
}
