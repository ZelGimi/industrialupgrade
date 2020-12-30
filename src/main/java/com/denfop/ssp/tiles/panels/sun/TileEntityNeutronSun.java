package com.denfop.ssp.tiles.panels.sun;

import com.denfop.ssp.common.Configs;
import com.denfop.ssp.tiles.panels.entity.TileEntitySunPanel;

public class TileEntityNeutronSun extends TileEntitySunPanel {

	public TileEntityNeutronSun() {
		super(Configs.Panel.sun[ Configs.Panel.Neutron.ordinal() ]);
	}
}
