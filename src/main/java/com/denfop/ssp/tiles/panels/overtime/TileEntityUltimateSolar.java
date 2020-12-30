package com.denfop.ssp.tiles.panels.overtime;

import com.denfop.ssp.common.Configs;
import com.denfop.ssp.tiles.panels.entity.TileEntitySolarPanel;

public class TileEntityUltimateSolar extends TileEntitySolarPanel {
	public static SolarConfig settings;

	public TileEntityUltimateSolar() {
		super(Configs.Panel.solar[ Configs.Panel.Ultimate.ordinal() ]);
	}
}
