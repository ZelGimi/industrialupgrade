package com.denfop.ssp.tiles.panels.rain;

import com.denfop.ssp.tiles.panels.entity.TileEntityRainPanel;

public class TileEntityNeutronRain extends TileEntityRainPanel {
	public static SolarConfig settings;

	public TileEntityNeutronRain() {
		super(TileEntityNeutronRain.settings);
	}
}
