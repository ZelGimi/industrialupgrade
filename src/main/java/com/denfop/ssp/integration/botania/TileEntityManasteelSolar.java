package com.denfop.ssp.integration.botania;

import com.denfop.ssp.tiles.panels.entity.TileEntitySolarPanel;

public class TileEntityManasteelSolar extends TileEntitySolarPanel {
	public static TileEntitySolarPanel.SolarConfig settings;

	public TileEntityManasteelSolar() {
		super(TileEntityManasteelSolar.settings);
	}
}
