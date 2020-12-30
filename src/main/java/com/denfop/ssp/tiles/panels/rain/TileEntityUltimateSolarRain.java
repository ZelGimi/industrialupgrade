package com.denfop.ssp.tiles.panels.rain;

import com.denfop.ssp.common.Configs;
import com.denfop.ssp.tiles.panels.entity.TileEntityRainPanel;

public class TileEntityUltimateSolarRain extends TileEntityRainPanel {

	public TileEntityUltimateSolarRain() {
		super(Configs.Panel.rain[ Configs.Panel.Hybrid.ordinal() ]);
	}
}
