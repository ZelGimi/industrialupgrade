package com.denfop.tiles.reactors.graphite;

import com.denfop.api.multiblock.IMultiElement;

public interface ICooling extends IMultiElement {

    double work(double heat);

}
