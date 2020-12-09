package com.denfop.ssp.tiles.rainpanels;

import com.denfop.ssp.tiles.TileEntityRainPanel;

public class TileEntityAdminrain extends TileEntityRainPanel {
	public static SolarConfig settings;

	public TileEntityAdminrain() {
		super(TileEntityAdminrain.settings);
	}


}
