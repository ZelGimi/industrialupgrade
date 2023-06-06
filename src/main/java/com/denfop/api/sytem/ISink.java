package com.denfop.api.sytem;

import net.minecraft.util.EnumFacing;

public interface ISink extends IAcceptor {

    double getDemanded();

    double inject(EnumFacing var1, double var2, double var4);

    double getPerEnergy();

    double getPastEnergy();

    void setPastEnergy(double pastEnergy);

    void addPerEnergy(double setEnergy);

    void addTick(double tick);

    double getTick();

    boolean isSink();

}
