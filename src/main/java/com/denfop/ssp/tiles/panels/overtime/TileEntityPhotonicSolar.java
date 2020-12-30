package com.denfop.ssp.tiles.panels.overtime;

import com.denfop.ssp.common.Configs;
import com.denfop.ssp.tiles.panels.entity.TileEntitySolarPanel;

public class TileEntityPhotonicSolar extends TileEntitySolarPanel {

	public TileEntityPhotonicSolar() {
		super(Configs.Panel.solar[ Configs.Panel.Photonic.ordinal() ]);
	}


}
