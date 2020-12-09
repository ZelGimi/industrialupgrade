package com.denfop.ssp.tiles.panels.earth;

import com.denfop.ssp.tiles.panels.entity.TileEntityEarthPanel;

public class TileEntityPhotonicEarth extends TileEntityEarthPanel {
	public static SolarConfig settings;

	public TileEntityPhotonicEarth() {
		super(TileEntityPhotonicEarth.settings);
	}


}
