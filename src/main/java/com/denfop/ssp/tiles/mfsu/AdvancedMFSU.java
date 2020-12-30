package com.denfop.ssp.tiles.mfsu;

import com.denfop.ssp.common.Configs;

public class AdvancedMFSU extends BaseMFSU {
	public AdvancedMFSU() {
		super(Configs.MFSU.Advanced.getTransferLimit(), Configs.MFSU.Advanced.getTier(), Configs.MFSU.Advanced.getMaxCharge());
	}
}