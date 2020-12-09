package com.denfop.ssp.tiles.panels.earth;

import com.denfop.ssp.tiles.panels.entity.TileEntityEarthPanel;

public class TileEntitySingularearth extends TileEntityEarthPanel {
	public static TileEntityEarthPanel.SolarConfig settings;

	public TileEntitySingularearth() {
		super(TileEntitySingularearth.settings);
	}
}
