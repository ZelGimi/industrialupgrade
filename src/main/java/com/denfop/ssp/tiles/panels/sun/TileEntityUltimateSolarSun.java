package com.denfop.ssp.tiles.panels.sun;

import com.denfop.ssp.common.Configs;
import com.denfop.ssp.tiles.panels.entity.TileEntitySunPanel;

public class TileEntityUltimateSolarSun extends TileEntitySunPanel {
	public static SolarConfig settings;

	public TileEntityUltimateSolarSun() {
		super(Configs.Panel.sun[ Configs.Panel.Ultimate.ordinal() ]);
	}
}
