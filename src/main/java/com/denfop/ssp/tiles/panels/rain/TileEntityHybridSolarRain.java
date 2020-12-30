package com.denfop.ssp.tiles.panels.rain;

import com.denfop.ssp.common.Configs;
import com.denfop.ssp.tiles.panels.entity.TileEntityRainPanel;

public class TileEntityHybridSolarRain extends TileEntityRainPanel {

	public TileEntityHybridSolarRain() {
		super(Configs.Panel.rain[ Configs.Panel.Hybrid.ordinal() ]);
	}
}
