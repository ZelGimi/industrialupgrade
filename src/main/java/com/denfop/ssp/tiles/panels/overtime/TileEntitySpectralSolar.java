package com.denfop.ssp.tiles.panels.overtime;

import com.denfop.ssp.common.Configs;
import com.denfop.ssp.tiles.panels.entity.TileEntitySolarPanel;

public class TileEntitySpectralSolar extends TileEntitySolarPanel {
	public static TileEntitySolarPanel.SolarConfig settings;

	public TileEntitySpectralSolar() {
		super(Configs.Panel.solar[ Configs.Panel.Spectral.ordinal() ]);
	}
}
