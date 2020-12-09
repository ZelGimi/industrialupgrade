package com.denfop.ssp.tiles.panels.fire;

import com.denfop.ssp.tiles.panels.entity.TileEntityNetherPanel;

public class TileEntitySingularfire extends TileEntityNetherPanel {
	public static TileEntityNetherPanel.SolarConfig settings;

	public TileEntitySingularfire() {
		super(TileEntitySingularfire.settings);
	}
}
