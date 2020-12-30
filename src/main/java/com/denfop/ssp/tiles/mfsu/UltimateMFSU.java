package com.denfop.ssp.tiles.mfsu;

import com.denfop.ssp.common.Configs;

public class UltimateMFSU extends BaseMFSU {
	public UltimateMFSU() {
		super(Configs.MFSU.Ultimate.getTransferLimit(), Configs.MFSU.Ultimate.getTier(), Configs.MFSU.Ultimate.getMaxCharge());
	}
}
