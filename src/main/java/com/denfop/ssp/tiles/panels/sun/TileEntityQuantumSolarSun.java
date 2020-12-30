package com.denfop.ssp.tiles.panels.sun;

import com.denfop.ssp.common.Configs;
import com.denfop.ssp.tiles.panels.entity.TileEntitySunPanel;

public class TileEntityQuantumSolarSun extends TileEntitySunPanel {

	public TileEntityQuantumSolarSun() {
		super(Configs.Panel.sun[ Configs.Panel.Quantum.ordinal() ]);
	}
}
