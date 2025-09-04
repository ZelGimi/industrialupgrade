package com.denfop.blockentity.chemicalplant;

import com.denfop.api.multiblock.MultiBlockElement;
import com.denfop.componets.ComponentBaseEnergy;

public interface IGenerator extends MultiBlockElement {

    ComponentBaseEnergy getEnergy();

}
