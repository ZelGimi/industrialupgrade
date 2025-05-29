package com.denfop.api.energy;


public interface ITransformer {

    Mode getMode();

    boolean isStepUp();

    double getinputflow();

    double getoutputflow();

}
