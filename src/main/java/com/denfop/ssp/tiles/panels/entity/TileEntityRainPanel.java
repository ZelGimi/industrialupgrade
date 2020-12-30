package com.denfop.ssp.tiles.panels.entity;


import com.denfop.ssp.common.Constants;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.core.init.Localization;
import ic2.core.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;

import javax.annotation.Nonnull;

public abstract class TileEntityRainPanel extends BasePanelTE {
	protected final int rainPower;

	public TileEntityRainPanel(SolarConfig config) {
		super(config);
		this.rainPower = config.rainPower;
	}

	@Nonnull
	@Override
	protected String getGuiDef() {
		return "solar_panel_rain";
	}

	protected void onLoaded() {
		super.onLoaded();
		if (!this.world.isRemote) {
			this.addedToEnet = !MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
			this.canRain = (this.world.getBiome(this.pos).canRain() || this.world.getBiome(this.pos).getRainfall() > 0.0F);
			this.hasSky = !this.world.provider.isNether();
		}
	}

	protected void onUnloaded() {
		super.onUnloaded();
		if (this.addedToEnet)
			this.addedToEnet = MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
	}

	@Override
	protected void updateEntityServer() {
		super.updateEntityServer();
		if (this.ticker++ % tickRate() == 0)
			checkTheSky();

		switch (this.active) {
			case DAY:
				tryGenerateEnergy(0);
				break;
			case RAIN:
				tryGenerateEnergy(this.rainPower);
				break;

		}

		if (this.storage > 0)
			this.storage = (int) (this.storage - this.chargeSlots.charge(this.storage));
	}

	@Override
	public void checkTheSky() {
		final BlockPos up = this.pos.up();
		if (this.hasSky && this.world.canBlockSeeSky(up) && this.world.getBlockState(up).getMaterial().getMaterialMapColor() == MapColor.AIR) {
			if (!this.canRain || (!this.world.isRaining() && !this.world.isThundering())) {
				this.active = GenerationState.NONE;
			} else {
				this.active = GenerationState.RAIN;
			}
		} else {
			this.active = GenerationState.NONE;
		}
	}

	public boolean getGuiState(String name) {
		if ("sunlight".equals(name))
			return (this.active == GenerationState.RAIN);
		if ("moonlight".equals(name))
			return (this.active == GenerationState.RAIN);
		return super.getGuiState(name);
	}

	@Override
	public String getOutput() {
		if (this.active == GenerationState.RAIN) {
			return String.format("%s %d %s", Localization.translate(Constants.MOD_ID + ".gui.generating"), this.rainPower, Localization.translate("ic2.generic.text.EUt"));
		}
		return String.format("%s 0 %s", Localization.translate(Constants.MOD_ID + ".gui.generating"), Localization.translate("ic2.generic.text.EUt"));
	}

	public static class SolarConfig extends BasePanelTE.SolarConfig {

		private final int rainPower;

		public SolarConfig(int rainPower, int maxStorage, int tier) {
			super(maxStorage, tier);
			this.rainPower = rainPower;
		}
	}
}
