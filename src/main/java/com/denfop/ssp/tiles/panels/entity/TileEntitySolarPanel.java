package com.denfop.ssp.tiles.panels.entity;


import com.denfop.ssp.common.Constants;
import ic2.core.init.Localization;
import ic2.core.util.Util;

import javax.annotation.Nonnull;

public abstract class TileEntitySolarPanel extends BasePanelTE {

    protected final int dayPower;

    protected final int nightPower;

    public TileEntitySolarPanel(SolarConfig config) {
        super(config);
        this.dayPower = config.dayPower;
        this.nightPower = config.nightPower;
    }

    @Nonnull
    @Override
    protected String getGuiDef() {
        return "solar_panel_overtime";
    }

    protected void updateEntityServer() {
        super.updateEntityServer();

        switch (this.active) {
            case DAY:
                tryGenerateEnergy(this.dayPower);
                break;
            case NIGHT:
                tryGenerateEnergy(this.nightPower);
                break;

        }
        if (this.storage > 0) {
            this.storage = (int) (this.storage - this.chargeSlots.charge(this.storage));
        }
    }

    @Override
    public void checkTheSky() {
        this.active = canSeeSky(this.pos.up()) ? this.world.isDaytime() &&
                !(this.canRain && (this.world.isRaining() || this.world.isThundering())) ?
                GenerationState.DAY : GenerationState.NIGHT : GenerationState.NONE;
    }

    public boolean getGuiState(String name) {
        if ("sunlight".equals(name)) {
            return (this.active == GenerationState.DAY);
        }
        if ("moonlight".equals(name)) {
            return (this.active == GenerationState.NIGHT);
        }
        return super.getGuiState(name);
    }

    @Override
    public String getOutput() {
        switch (this.active) {
            case DAY:
                return String.format(
                        "%s %s %s",
                        Localization.translate(Constants.MOD_ID + ".gui.generating"),
                        Util.toSiString(this.dayPower, 3),
                        Localization.translate("ic2.generic.text.EUt")
                );
            case NIGHT:
                return String.format(
                        "%s %s %s",
                        Localization.translate(Constants.MOD_ID + ".gui.generating"),
                        Util.toSiString(this.nightPower, 3),
                        Localization.translate("ic2.generic.text.EUt")
                );
        }
        return String.format(
                "%s 0 %s",
                Localization.translate(Constants.MOD_ID + ".gui.generating"),
                Localization.translate("ic2.generic.text.EUt")
        );
    }

    public static class SolarConfig extends BasePanelTE.SolarConfig {

        private final int dayPower;
        private final int nightPower;

        public SolarConfig(int dayPower, int nightPower, int maxStorage, int tier) {
            super(maxStorage, tier);
            this.dayPower = dayPower;
            this.nightPower = nightPower;
        }

    }

}
