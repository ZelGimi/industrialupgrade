package com.denfop.blockentity.reactors.gas;

import com.denfop.api.multiblock.IMultiElement;

public interface IRegenerator extends IMultiElement {

    int getMaxHelium();

    int getHelium();

    void addHelium(int helium);

}
