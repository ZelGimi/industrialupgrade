package com.denfop.ssp.tiles.panels.end;

import com.denfop.ssp.tiles.panels.entity.TileEntityEnderPanel;

public class TileEntitySingularend extends TileEntityEnderPanel {
	public static TileEntityEnderPanel.SolarConfig settings;

	public TileEntitySingularend() {
		super(TileEntitySingularend.settings);
	}
}
