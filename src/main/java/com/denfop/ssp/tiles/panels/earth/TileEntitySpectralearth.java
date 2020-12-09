package com.denfop.ssp.tiles.panels.earth;

import com.denfop.ssp.tiles.panels.entity.TileEntityEarthPanel;

public class TileEntitySpectralearth extends TileEntityEarthPanel {
	public static TileEntityEarthPanel.SolarConfig settings;

	public TileEntitySpectralearth() {
		super(TileEntitySpectralearth.settings);
	}
}
