package com.denfop.ssp.tiles.panels.air;

import com.denfop.ssp.tiles.panels.entity.TileEntityAirPanel;

public class TileEntityPhotonicAir extends TileEntityAirPanel {
	public static SolarConfig settings;

	public TileEntityPhotonicAir() {
		super(TileEntityPhotonicAir.settings);
	}


}
