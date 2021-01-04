package com.denfop.ssp.tiles.panels.entity;


import com.denfop.ssp.common.Constants;
import ic2.core.init.Localization;
import ic2.core.util.Util;

import javax.annotation.Nonnull;

public abstract class TileEntitySunPanel extends BasePanelTE {
	protected final int dayPower;

	public TileEntitySunPanel(SolarConfig config) {
		super(config);
		this.dayPower = config.dayPower;
	}

	@Nonnull
	@Override
	protected String getGuiDef() {
		return "solar_panel_sun";
	}

	protected void updateEntityServer() {
		super.updateEntityServer();

		if (this.active == GenerationState.DAY) {
			tryGenerateEnergy(this.dayPower);
		}
		if (this.storage > 0)
			this.storage = (int) (this.storage - this.chargeSlots.charge(this.storage));
	}

	public boolean getGuiState(String name) {
		if ("sunlight".equals(name))
			return (this.active == GenerationState.DAY);

		return super.getGuiState(name);
	}

	@Override
	public void checkTheSky() {
		this.active = !canSeeSky(this.pos.up()) ||
				!this.world.isDaytime() || this.canRain &&
				(this.world.isRaining() || this.world.isThundering()) ?
				GenerationState.NONE : GenerationState.DAY;
	}

	@Override
	public String getOutput() {
		if (this.active == GenerationState.DAY) {
			return String.format("%s %s %s", Localization.translate(Constants.MOD_ID + ".gui.generating"), Util.toSiString(this.dayPower, 3), Localization.translate("ic2.generic.text.EUt"));
		}
		return String.format("%s 0 %s", Localization.translate(Constants.MOD_ID + ".gui.generating"), Localization.translate("ic2.generic.text.EUt"));
	}

	public static class SolarConfig extends BasePanelTE.SolarConfig {

		private final int dayPower;

		public SolarConfig(int dayPower, int maxStorage, int tier) {
			super(maxStorage, tier);
			this.dayPower = dayPower;
		}
	}
}
