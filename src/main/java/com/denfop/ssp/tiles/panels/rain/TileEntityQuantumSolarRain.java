package com.denfop.ssp.tiles.panels.rain;

import com.denfop.ssp.common.Configs;
import com.denfop.ssp.tiles.panels.entity.TileEntityRainPanel;

public class TileEntityQuantumSolarRain extends TileEntityRainPanel {

	public TileEntityQuantumSolarRain() {
		super(Configs.Panel.rain[ Configs.Panel.Quantum.ordinal() ]);
	}
}
