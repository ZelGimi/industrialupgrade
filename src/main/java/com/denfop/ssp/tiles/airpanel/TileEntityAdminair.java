package com.denfop.ssp.tiles.airpanel;

import com.denfop.ssp.tiles.TileEntityAirPanel;

public class TileEntityAdminair extends TileEntityAirPanel {
	public static SolarConfig settings;

	public TileEntityAdminair() {
		super(TileEntityAdminair.settings);
	}


}
