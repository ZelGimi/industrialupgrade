package com.denfop.ssp.tiles.overtimepanel;

import com.denfop.ssp.tiles.TileEntitySolarPanel;

public class TileEntityUltimateHybridSolar extends TileEntitySolarPanel {
	public static SolarConfig settings;

	public TileEntityUltimateHybridSolar() {
		super(TileEntityUltimateHybridSolar.settings);
	}
}
