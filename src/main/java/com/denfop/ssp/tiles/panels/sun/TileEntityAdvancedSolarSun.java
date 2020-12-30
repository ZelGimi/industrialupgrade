package com.denfop.ssp.tiles.panels.sun;

import com.denfop.ssp.common.Configs;
import com.denfop.ssp.tiles.panels.entity.TileEntitySunPanel;

public class TileEntityAdvancedSolarSun extends TileEntitySunPanel {

	public TileEntityAdvancedSolarSun() {
		super(Configs.Panel.sun[ Configs.Panel.Advanced.ordinal() ]);
	}
}
