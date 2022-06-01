package com.denfop.api;

import ic2.api.energy.IEnergyNet;

public interface IAdvEnergyNet extends IEnergyNet {


    double getPowerFromTier(int var1);

    int getTierFromPower(double var1);


    double getRFFromEU(int amount);


}
