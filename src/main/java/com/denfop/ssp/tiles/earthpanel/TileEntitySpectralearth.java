package com.denfop.ssp.tiles.earthpanel;

import com.denfop.ssp.tiles.TileEntityEarthPanel;

public class TileEntitySpectralearth extends TileEntityEarthPanel {
	public static TileEntityEarthPanel.SolarConfig settings;

	public TileEntitySpectralearth() {
		super(TileEntitySpectralearth.settings);
	}
}
