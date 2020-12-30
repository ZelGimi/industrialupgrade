package com.denfop.ssp.tiles.panels.rain;

import com.denfop.ssp.common.Configs;
import com.denfop.ssp.tiles.panels.entity.TileEntityRainPanel;

public class TileEntityNeutronRain extends TileEntityRainPanel {

	public TileEntityNeutronRain() {
		super(Configs.Panel.rain[ Configs.Panel.Neutron.ordinal() ]);
	}
}
