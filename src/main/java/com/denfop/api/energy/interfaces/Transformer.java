package com.denfop.api.energy.interfaces;


import com.denfop.api.energy.utils.Mode;

public interface Transformer {

    Mode getMode();

    boolean isStepUp();

    double getinputflow();

    double getoutputflow();

}
