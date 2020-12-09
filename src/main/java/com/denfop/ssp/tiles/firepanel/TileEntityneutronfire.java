package com.denfop.ssp.tiles.firepanel;

import com.denfop.ssp.tiles.TileEntityNetherPanel;

public class TileEntityneutronfire extends TileEntityNetherPanel {
	public static SolarConfig settings;

	public TileEntityneutronfire() {
		super(TileEntityneutronfire.settings);
	}
}
