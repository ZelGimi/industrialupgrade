package com.denfop.ssp.tiles.panels.overtime;

import com.denfop.ssp.common.Configs;
import com.denfop.ssp.tiles.panels.entity.TileEntitySolarPanel;

public class TileEntityNeutronSolar extends TileEntitySolarPanel {

	public TileEntityNeutronSolar() {
		super(Configs.Panel.solar[ Configs.Panel.Neutron.ordinal() ]);
	}
}
