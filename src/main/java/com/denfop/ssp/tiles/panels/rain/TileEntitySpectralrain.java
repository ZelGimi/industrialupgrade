package com.denfop.ssp.tiles.panels.rain;

import com.denfop.ssp.tiles.panels.entity.TileEntityRainPanel;

public class TileEntitySpectralrain extends TileEntityRainPanel {
	public static TileEntityRainPanel.SolarConfig settings;

	public TileEntitySpectralrain() {
		super(TileEntitySpectralrain.settings);
	}
}
