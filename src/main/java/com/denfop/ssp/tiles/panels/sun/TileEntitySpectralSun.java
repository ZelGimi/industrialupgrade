package com.denfop.ssp.tiles.panels.sun;

import com.denfop.ssp.tiles.panels.entity.TileEntitySunPanel;

public class TileEntitySpectralSun extends TileEntitySunPanel {
	public static TileEntitySunPanel.SolarConfig settings;

	public TileEntitySpectralSun() {
		super(TileEntitySpectralSun.settings);
	}
}
