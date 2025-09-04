package com.denfop.blockentity.reactors.heat;

import com.denfop.api.multiblock.MultiBlockElement;

public interface ICoolant extends MultiBlockElement {

    void addHeliumToRegenerate(double col);

    double getMaxRegenerate();

    double getHeliumToRegenerate();

}
