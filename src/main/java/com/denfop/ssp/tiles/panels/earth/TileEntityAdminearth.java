package com.denfop.ssp.tiles.panels.earth;

import com.denfop.ssp.tiles.panels.entity.TileEntityEarthPanel;

public class TileEntityAdminearth extends TileEntityEarthPanel {
	public static SolarConfig settings;

	public TileEntityAdminearth() {
		super(TileEntityAdminearth.settings);
	}


}
