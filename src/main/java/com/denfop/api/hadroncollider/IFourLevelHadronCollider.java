package com.denfop.api.hadroncollider;

public interface IFourLevelHadronCollider extends IThirdLevelHadronCollider {

    IHadronEnergyBlock getHCEnergy();

    IConverterHCE getConverter();

    ISensors getSensors();

}
