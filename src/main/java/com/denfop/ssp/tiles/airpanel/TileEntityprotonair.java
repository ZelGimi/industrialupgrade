package com.denfop.ssp.tiles.airpanel;

import com.denfop.ssp.tiles.TileEntityAirPanel;

public class TileEntityprotonair extends TileEntityAirPanel {
	public static TileEntityAirPanel.SolarConfig settings;

	public TileEntityprotonair() {
		super(TileEntityprotonair.settings);
	}
}
