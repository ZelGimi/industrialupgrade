package com.denfop.ssp.tiles.panels.overtime;

import com.denfop.ssp.common.Configs;
import com.denfop.ssp.tiles.panels.entity.TileEntitySolarPanel;

public class TileEntityQuantumSolar extends TileEntitySolarPanel {

	public TileEntityQuantumSolar() {
		super(Configs.Panel.solar[ Configs.Panel.Quantum.ordinal() ]);
	}
}
