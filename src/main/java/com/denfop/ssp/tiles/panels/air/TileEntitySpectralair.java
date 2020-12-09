package com.denfop.ssp.tiles.panels.air;

import com.denfop.ssp.tiles.panels.entity.TileEntityAirPanel;

public class TileEntitySpectralair extends TileEntityAirPanel {
	public static TileEntityAirPanel.SolarConfig settings;

	public TileEntitySpectralair() {
		super(TileEntitySpectralair.settings);
	}
}
