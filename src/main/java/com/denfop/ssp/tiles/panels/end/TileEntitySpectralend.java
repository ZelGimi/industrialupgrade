package com.denfop.ssp.tiles.panels.end;

import com.denfop.ssp.tiles.panels.entity.TileEntityEnderPanel;

public class TileEntitySpectralend extends TileEntityEnderPanel {
	public static TileEntityEnderPanel.SolarConfig settings;

	public TileEntitySpectralend() {
		super(TileEntitySpectralend.settings);
	}
}
