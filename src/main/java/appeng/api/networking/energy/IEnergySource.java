package appeng.api.networking.energy;

import appeng.api.config.Actionable;
import appeng.api.config.PowerMultiplier;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

public interface IEnergySource {

    @Nonnegative
    double extractAEPower(@Nonnegative double var1, @Nonnull Actionable var3, @Nonnull PowerMultiplier var4);

}
