package appeng.api.networking.energy;

import appeng.api.config.AccessRestriction;
import appeng.api.config.Actionable;

import javax.annotation.Nonnull;

public interface IAEPowerStorage extends IEnergySource {

    double injectAEPower(double var1, @Nonnull Actionable var3);

    double getAEMaxPower();

    double getAECurrentPower();

    boolean isAEPublicPowerStorage();

    @Nonnull
    AccessRestriction getPowerFlow();

}
