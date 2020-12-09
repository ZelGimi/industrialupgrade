package com.denfop.ssp.tiles.panels.rain;

import com.denfop.ssp.tiles.panels.entity.TileEntityRainPanel;

public class TileEntityAdminrain extends TileEntityRainPanel {
	public static SolarConfig settings;

	public TileEntityAdminrain() {
		super(TileEntityAdminrain.settings);
	}


}
