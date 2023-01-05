package com.denfop.api.qe;

import net.minecraft.util.EnumFacing;

public interface IQESink extends IQEAcceptor {

    double getDemandedQE();

    double injectQE(EnumFacing var1, double var2, double var4);

    double getPerEnergy();

    double getPastEnergy();

    void setPastEnergy(double pastEnergy);

    void addPerEnergy(double setEnergy);

    void addTick(double tick);

    double getTick();

    boolean isSink();

}
