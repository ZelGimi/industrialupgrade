package com.denfop.api.energy;

import net.minecraft.core.Direction;

import java.util.List;

public interface IEnergySink extends IEnergyAcceptor {

    double getPerEnergy();

    double getPastEnergy();

    void setPastEnergy(double pastEnergy);

    void addPerEnergy(double setEnergy);

    void addTick(double tick);

    double getTick();

    boolean isSink();

    double getDemandedEnergy();

    default double getDemandedEnergy(Direction direction) {
        return getDemandedEnergy();
    }

    ;

    int getSinkTier();

    default int getSinkTier(Direction direction) {
        return getSinkTier();
    }

    ;

    void receiveEnergy(double var2);

    default void receiveEnergy(Direction direction, double var2) {
        receiveEnergy(var2);
    }

    ;

    List<Integer> getEnergyTickList();


}
