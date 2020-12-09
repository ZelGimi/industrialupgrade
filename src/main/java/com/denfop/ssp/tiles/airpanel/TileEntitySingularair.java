package com.denfop.ssp.tiles.airpanel;

import com.denfop.ssp.tiles.TileEntityAirPanel;

public class TileEntitySingularair extends TileEntityAirPanel {
	public static TileEntityAirPanel.SolarConfig settings;

	public TileEntitySingularair() {
		super(TileEntitySingularair.settings);
	}
}
