package com.denfop.blockentity.windturbine;

import com.denfop.api.multiblock.MultiBlockElement;
import com.denfop.componets.Energy;

public interface ISocket extends MultiBlockElement {

    Energy getEnergy();

}
