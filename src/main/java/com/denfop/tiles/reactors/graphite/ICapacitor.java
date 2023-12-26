package com.denfop.tiles.reactors.graphite;

import com.denfop.api.multiblock.IMultiElement;

public interface ICapacitor extends IMultiElement {
    double getPercent(int x);
}
