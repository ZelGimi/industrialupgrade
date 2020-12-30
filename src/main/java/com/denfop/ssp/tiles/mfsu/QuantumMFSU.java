package com.denfop.ssp.tiles.mfsu;

import com.denfop.ssp.common.Configs;

public class QuantumMFSU extends BaseMFSU {
	public QuantumMFSU() {
		super(Configs.MFSU.Quantum.getTransferLimit(), Configs.MFSU.Quantum.getTier(), Configs.MFSU.Quantum.getMaxCharge());
	}
}