package com.denfop.blockentity.reactors.gas;

import com.denfop.api.multiblock.MultiBlockElement;

public interface IRegenerator extends MultiBlockElement {

    int getMaxHelium();

    int getHelium();

    void addHelium(int helium);

}
