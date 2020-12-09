package com.denfop.ssp.tiles.panels.air;

import com.denfop.ssp.tiles.panels.entity.TileEntityAirPanel;

public class TileEntityAdminair extends TileEntityAirPanel {
	public static SolarConfig settings;

	public TileEntityAdminair() {
		super(TileEntityAdminair.settings);
	}


}
