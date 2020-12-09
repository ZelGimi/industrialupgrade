package com.denfop.ssp.tiles.panels.end;

import com.denfop.ssp.tiles.panels.entity.TileEntityEnderPanel;

public class TileEntityProtonEnd extends TileEntityEnderPanel {
	public static TileEntityEnderPanel.SolarConfig settings;

	public TileEntityProtonEnd() {
		super(TileEntityProtonEnd.settings);
	}
}
