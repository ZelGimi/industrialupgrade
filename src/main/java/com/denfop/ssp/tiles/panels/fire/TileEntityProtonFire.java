package com.denfop.ssp.tiles.panels.fire;

import com.denfop.ssp.tiles.panels.entity.TileEntityNetherPanel;

public class TileEntityProtonFire extends TileEntityNetherPanel {
	public static TileEntityNetherPanel.SolarConfig settings;

	public TileEntityProtonFire() {
		super(TileEntityProtonFire.settings);
	}
}
