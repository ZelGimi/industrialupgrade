package com.denfop.ssp.tiles.panels.sun;

import com.denfop.ssp.common.Configs;
import com.denfop.ssp.tiles.panels.entity.TileEntitySunPanel;

public class TileEntitySingularSun extends TileEntitySunPanel {

	public TileEntitySingularSun() {
		super(Configs.Panel.sun[ Configs.Panel.Singular.ordinal() ]);
	}
}
