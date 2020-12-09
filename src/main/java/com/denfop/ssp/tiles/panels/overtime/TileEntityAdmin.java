package com.denfop.ssp.tiles.panels.overtime;

import com.denfop.ssp.tiles.panels.entity.TileEntitySolarPanel;

public class TileEntityAdmin extends TileEntitySolarPanel {
	public static TileEntitySolarPanel.SolarConfig settings;

	public TileEntityAdmin() {
		super(TileEntityAdmin.settings);
	}


}
