package com.denfop.ssp.tiles.panels.rain;

import com.denfop.ssp.common.Configs;
import com.denfop.ssp.tiles.panels.entity.TileEntityRainPanel;

public class TileEntitySpectralRain extends TileEntityRainPanel {

	public TileEntitySpectralRain() {
		super(Configs.Panel.rain[ Configs.Panel.Spectral.ordinal() ]);
	}
}
