package com.denfop.ssp.tiles.panels.air;

import com.denfop.ssp.tiles.panels.entity.TileEntityAirPanel;

public class TileEntityProtonAir extends TileEntityAirPanel {
	public static TileEntityAirPanel.SolarConfig settings;

	public TileEntityProtonAir() {
		super(TileEntityProtonAir.settings);
	}
}
