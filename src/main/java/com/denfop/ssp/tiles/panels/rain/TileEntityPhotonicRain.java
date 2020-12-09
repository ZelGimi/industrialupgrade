package com.denfop.ssp.tiles.panels.rain;

import com.denfop.ssp.tiles.panels.entity.TileEntityRainPanel;

public class TileEntityPhotonicRain extends TileEntityRainPanel {
	public static SolarConfig settings;

	public TileEntityPhotonicRain() {
		super(TileEntityPhotonicRain.settings);
	}


}
