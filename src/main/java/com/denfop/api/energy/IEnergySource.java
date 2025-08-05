package com.denfop.api.energy;

import net.minecraft.core.Direction;

public interface IEnergySource extends IEnergyEmitter {

    double getPerEnergy();

    double getPastEnergy();

    void setPastEnergy(double pastEnergy);

    void addPerEnergy(double setEnergy);

    boolean isSource();
    default double canExtractEnergy(Direction direction){
       return canExtractEnergy();
    }
    double canExtractEnergy();
    default void extractEnergy(Direction direction, double var1){
        extractEnergy(var1);
    }
    void extractEnergy(double var1);
    default  int getSourceTier(Direction direction){
        return getSourceTier();
    }
    int getSourceTier();

}
