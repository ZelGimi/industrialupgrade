package com.denfop.ssp.tiles.panels.air;

import com.denfop.ssp.tiles.panels.entity.TileEntityAirPanel;

public class TileEntityNeutronAir extends TileEntityAirPanel {
	public static SolarConfig settings;

	public TileEntityNeutronAir() {
		super(TileEntityNeutronAir.settings);
	}
}
