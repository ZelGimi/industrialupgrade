package com.denfop.ssp.tiles.panels.end;

import com.denfop.ssp.tiles.panels.entity.TileEntityEnderPanel;

public class TileEntityAdminend extends TileEntityEnderPanel {
	public static SolarConfig settings;

	public TileEntityAdminend() {
		super(TileEntityAdminend.settings);
	}


}
