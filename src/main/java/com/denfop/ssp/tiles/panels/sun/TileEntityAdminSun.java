package com.denfop.ssp.tiles.panels.sun;

import com.denfop.ssp.tiles.panels.entity.TileEntitySunPanel;

public class TileEntityAdminSun extends TileEntitySunPanel {
	public static TileEntitySunPanel.SolarConfig settings;

	public TileEntityAdminSun() {
		super(TileEntityAdminSun.settings);
	}


}
