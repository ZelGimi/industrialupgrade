package com.denfop.ssp.tiles.panels.moon;

import com.denfop.ssp.tiles.panels.entity.TileEntityMoonPanel;

public class TileEntitySpectralmoon extends TileEntityMoonPanel {
	public static TileEntityMoonPanel.SolarConfig settings;

	public TileEntitySpectralmoon() {
		super(TileEntitySpectralmoon.settings);
	}
}
