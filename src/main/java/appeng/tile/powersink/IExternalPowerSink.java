package appeng.tile.powersink;

import appeng.api.config.Actionable;
import appeng.api.config.PowerUnits;
import appeng.api.networking.energy.IAEPowerStorage;

public interface IExternalPowerSink extends IAEPowerStorage {

    double injectExternalPower(PowerUnits var1, double var2, Actionable var4);

    double getExternalPowerDemand(PowerUnits var1, double var2);

}
