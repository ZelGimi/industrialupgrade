package com.denfop.ssp.tiles.firepanel;

import com.denfop.ssp.tiles.TileEntityNetherPanel;

public class TileEntitySpectralfire extends TileEntityNetherPanel {
	public static TileEntityNetherPanel.SolarConfig settings;

	public TileEntitySpectralfire() {
		super(TileEntitySpectralfire.settings);
	}
}
