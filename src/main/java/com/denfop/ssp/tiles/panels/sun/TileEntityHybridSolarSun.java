package com.denfop.ssp.tiles.panels.sun;

import com.denfop.ssp.common.Configs;
import com.denfop.ssp.tiles.panels.entity.TileEntitySunPanel;

public class TileEntityHybridSolarSun extends TileEntitySunPanel {

	public TileEntityHybridSolarSun() {
		super(Configs.Panel.sun[ Configs.Panel.Hybrid.ordinal() ]);
	}
}
