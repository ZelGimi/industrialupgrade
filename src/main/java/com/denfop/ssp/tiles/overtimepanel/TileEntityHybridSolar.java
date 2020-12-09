package com.denfop.ssp.tiles.overtimepanel;

import com.denfop.ssp.tiles.TileEntitySolarPanel;

public class TileEntityHybridSolar extends TileEntitySolarPanel {
	public static SolarConfig settings;

	public TileEntityHybridSolar() {
		super(TileEntityHybridSolar.settings);
	}
}
