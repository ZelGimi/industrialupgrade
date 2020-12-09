package com.denfop.ssp.tiles.panels.end;

import com.denfop.ssp.tiles.panels.entity.TileEntityEnderPanel;

public class TileEntityNeutronEnd extends TileEntityEnderPanel {
	public static SolarConfig settings;

	public TileEntityNeutronEnd() {
		super(TileEntityNeutronEnd.settings);
	}
}
