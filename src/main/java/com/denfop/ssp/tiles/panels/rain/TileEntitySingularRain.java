package com.denfop.ssp.tiles.panels.rain;

import com.denfop.ssp.common.Configs;
import com.denfop.ssp.tiles.panels.entity.TileEntityRainPanel;

public class TileEntitySingularRain extends TileEntityRainPanel {

	public TileEntitySingularRain() {
		super(Configs.Panel.rain[ Configs.Panel.Singular.ordinal() ]);
	}
}
