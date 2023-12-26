package com.denfop.tiles.reactors.gas;

import com.denfop.api.multiblock.IMultiElement;

public interface ICompressor extends IMultiElement {

    int getEnergy();

    int getPressure();
}
