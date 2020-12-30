package com.denfop.ssp.tiles.panels.overtime;

import com.denfop.ssp.common.Configs;
import com.denfop.ssp.tiles.panels.entity.TileEntitySolarPanel;

public class TileEntityAdvancedSolar extends TileEntitySolarPanel {

	public TileEntityAdvancedSolar() {
		super(Configs.Panel.solar[ Configs.Panel.Advanced.ordinal() ]);
	}
}
