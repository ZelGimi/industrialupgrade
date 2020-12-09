package com.denfop.ssp.tiles.panels.rain;

import com.denfop.ssp.tiles.panels.entity.TileEntityRainPanel;

public class TileEntityProtonRain extends TileEntityRainPanel {
	public static TileEntityRainPanel.SolarConfig settings;

	public TileEntityProtonRain() {
		super(TileEntityProtonRain.settings);
	}
}
