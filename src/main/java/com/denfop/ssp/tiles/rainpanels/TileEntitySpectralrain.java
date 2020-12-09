package com.denfop.ssp.tiles.rainpanels;

import com.denfop.ssp.tiles.TileEntityRainPanel;

public class TileEntitySpectralrain extends TileEntityRainPanel {
	public static TileEntityRainPanel.SolarConfig settings;

	public TileEntitySpectralrain() {
		super(TileEntitySpectralrain.settings);
	}
}
