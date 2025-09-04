package com.denfop.blockentity.reactors.graphite;

import com.denfop.api.multiblock.MultiBlockElement;

public interface ICooling extends MultiBlockElement {

    double work(double heat);

}
