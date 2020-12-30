package com.denfop.ssp.tiles.panels.overtime;

import com.denfop.ssp.common.Configs;
import com.denfop.ssp.tiles.panels.entity.TileEntitySolarPanel;

public class TileEntityProtonSolar extends TileEntitySolarPanel {

	public TileEntityProtonSolar() {
		super(Configs.Panel.solar[ Configs.Panel.Proton.ordinal() ]);
	}
}
