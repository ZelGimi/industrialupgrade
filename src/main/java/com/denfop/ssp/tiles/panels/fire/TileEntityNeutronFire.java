package com.denfop.ssp.tiles.panels.fire;

import com.denfop.ssp.tiles.panels.entity.TileEntityNetherPanel;

public class TileEntityNeutronFire extends TileEntityNetherPanel {
	public static SolarConfig settings;

	public TileEntityNeutronFire() {
		super(TileEntityNeutronFire.settings);
	}
}
