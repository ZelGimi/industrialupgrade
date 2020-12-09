package com.denfop.ssp.tiles.panels.fire;

import com.denfop.ssp.tiles.panels.entity.TileEntityNetherPanel;

public class TileEntitySpectralfire extends TileEntityNetherPanel {
	public static TileEntityNetherPanel.SolarConfig settings;

	public TileEntitySpectralfire() {
		super(TileEntitySpectralfire.settings);
	}
}
