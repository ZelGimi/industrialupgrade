package com.denfop.ssp.tiles.panels.rain;

import com.denfop.ssp.common.Configs;
import com.denfop.ssp.tiles.panels.entity.TileEntityRainPanel;

public class TileEntityAdvancedSolarRain extends TileEntityRainPanel {
	public TileEntityAdvancedSolarRain() {
		super(Configs.Panel.rain[ Configs.Panel.Advanced.ordinal() ]);
	}
}
