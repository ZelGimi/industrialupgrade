package com.denfop.ssp.tiles.endpanel;

import com.denfop.ssp.tiles.TileEntityEnderPanel;

public class TileEntityprotonend extends TileEntityEnderPanel {
	public static TileEntityEnderPanel.SolarConfig settings;

	public TileEntityprotonend() {
		super(TileEntityprotonend.settings);
	}
}
