package com.denfop.ssp.tiles.panels.rain;

import com.denfop.ssp.tiles.panels.entity.TileEntityRainPanel;

public class TileEntityUltimateHybridSolarRain extends TileEntityRainPanel {
	public static SolarConfig settings;

	public TileEntityUltimateHybridSolarRain() {
		super(TileEntityUltimateHybridSolarRain.settings);
	}
}
