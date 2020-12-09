package com.denfop.ssp.tiles.endpanel;

import com.denfop.ssp.tiles.TileEntityEnderPanel;

public class TileEntitySpectralend extends TileEntityEnderPanel {
	public static TileEntityEnderPanel.SolarConfig settings;

	public TileEntitySpectralend() {
		super(TileEntitySpectralend.settings);
	}
}
