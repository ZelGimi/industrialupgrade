package com.denfop.ssp.tiles.panels.sun;

import com.denfop.ssp.common.Configs;
import com.denfop.ssp.tiles.panels.entity.TileEntitySunPanel;

public class TileEntityProtonSun extends TileEntitySunPanel {

	public TileEntityProtonSun() {
		super(Configs.Panel.sun[ Configs.Panel.Proton.ordinal() ]);
	}
}
