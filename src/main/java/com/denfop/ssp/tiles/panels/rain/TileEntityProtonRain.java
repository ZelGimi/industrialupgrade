package com.denfop.ssp.tiles.panels.rain;

import com.denfop.ssp.common.Configs;
import com.denfop.ssp.tiles.panels.entity.TileEntityRainPanel;

public class TileEntityProtonRain extends TileEntityRainPanel {
	public static TileEntityRainPanel.SolarConfig settings;

	public TileEntityProtonRain() {
		super(Configs.Panel.rain[ Configs.Panel.Proton.ordinal() ]);
	}
}
