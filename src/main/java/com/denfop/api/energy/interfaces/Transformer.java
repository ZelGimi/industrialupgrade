package com.denfop.api.energy.interfaces;


import com.denfop.api.energy.Mode;

public interface Transformer {

    Mode getMode();

    boolean isStepUp();

    double getinputflow();

    double getoutputflow();

}
