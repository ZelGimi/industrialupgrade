package com.denfop.ssp.tiles.panels.air;

import com.denfop.ssp.tiles.panels.entity.TileEntityAirPanel;

public class TileEntitySingularair extends TileEntityAirPanel {
	public static TileEntityAirPanel.SolarConfig settings;

	public TileEntitySingularair() {
		super(TileEntitySingularair.settings);
	}
}
