package com.denfop.tiles.reactors.heat;

import com.denfop.api.multiblock.IMultiElement;
import com.denfop.componets.AdvEnergy;

public interface ISocket extends IMultiElement {
    AdvEnergy getEnergy();
}
