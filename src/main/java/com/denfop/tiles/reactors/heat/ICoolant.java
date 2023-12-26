package com.denfop.tiles.reactors.heat;

import com.denfop.api.multiblock.IMultiElement;

public interface ICoolant extends IMultiElement {

    void addHeliumToRegenerate(double col);

    double getMaxRegenerate();

    double getHeliumToRegenerate();
}
