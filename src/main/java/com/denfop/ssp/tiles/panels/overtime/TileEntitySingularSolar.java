package com.denfop.ssp.tiles.panels.overtime;

import com.denfop.ssp.common.Configs;
import com.denfop.ssp.tiles.panels.entity.TileEntitySolarPanel;

public class TileEntitySingularSolar extends TileEntitySolarPanel {

	public TileEntitySingularSolar() {
		super(Configs.Panel.solar[ Configs.Panel.Singular.ordinal() ]);
	}
}
