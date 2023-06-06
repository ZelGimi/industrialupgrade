package com.denfop.api.energy;

import net.minecraft.util.EnumFacing;

public interface IAdvEnergySink extends IEnergyAcceptor {

    double getPerEnergy();

    double getPastEnergy();

    void setPastEnergy(double pastEnergy);

    void addPerEnergy(double setEnergy);

    void addTick(double tick);

    double getTick();

    boolean isSink();

    double getDemandedEnergy();

    int getSinkTier();

    double injectEnergy(EnumFacing var1, double var2, double var4);

}
