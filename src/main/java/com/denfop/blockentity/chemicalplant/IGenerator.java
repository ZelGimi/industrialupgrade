package com.denfop.blockentity.chemicalplant;

import com.denfop.api.multiblock.IMultiElement;
import com.denfop.componets.ComponentBaseEnergy;

public interface IGenerator extends IMultiElement {

    ComponentBaseEnergy getEnergy();

}
