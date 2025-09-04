package com.denfop.api.otherenergies.common.interfaces;

import java.util.List;

public interface Sink extends Acceptor {

    double getDemanded();

    void receivedEnergy(double var2);

    double getPerEnergy();

    double getPastEnergy();

    void setPastEnergy(double pastEnergy);

    void addPerEnergy(double setEnergy);

    void addTick(double tick);

    double getTick();

    boolean isSink();

    List<Source> getEnergyTickList();
}
