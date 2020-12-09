package com.denfop.ssp.tiles.firepanel;

import com.denfop.ssp.tiles.TileEntityNetherPanel;

public class TileEntitySingularfire extends TileEntityNetherPanel {
	public static TileEntityNetherPanel.SolarConfig settings;

	public TileEntitySingularfire() {
		super(TileEntitySingularfire.settings);
	}
}
