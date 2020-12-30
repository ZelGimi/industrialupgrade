package com.denfop.ssp.tiles.panels.sun;

import com.denfop.ssp.common.Configs;
import com.denfop.ssp.tiles.panels.entity.TileEntitySunPanel;

public class TileEntitySpectralSun extends TileEntitySunPanel {
	public static TileEntitySunPanel.SolarConfig settings;

	public TileEntitySpectralSun() {
		super(Configs.Panel.sun[ Configs.Panel.Spectral.ordinal() ]);
	}
}
