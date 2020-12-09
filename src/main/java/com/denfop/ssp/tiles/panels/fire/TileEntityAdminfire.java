package com.denfop.ssp.tiles.panels.fire;

import com.denfop.ssp.tiles.panels.entity.TileEntityNetherPanel;

public class TileEntityAdminfire extends TileEntityNetherPanel {
	public static SolarConfig settings;

	public TileEntityAdminfire() {
		super(TileEntityAdminfire.settings);
	}


}
