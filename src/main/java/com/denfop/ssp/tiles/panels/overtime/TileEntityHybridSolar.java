package com.denfop.ssp.tiles.panels.overtime;

import com.denfop.ssp.common.Configs;
import com.denfop.ssp.tiles.panels.entity.TileEntitySolarPanel;

public class TileEntityHybridSolar extends TileEntitySolarPanel {

	public TileEntityHybridSolar() {
		super(Configs.Panel.solar[ Configs.Panel.Hybrid.ordinal() ]);
	}
}
