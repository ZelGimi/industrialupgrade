package com.denfop.tiles.gasturbine;

import com.denfop.api.multiblock.IMultiElement;
import com.denfop.invslot.InvSlot;

public interface IRecuperator extends IMultiElement {

    InvSlot getExchanger();

    double getPower();

}
