package com.denfop.tiles.gasturbine;

import com.denfop.api.multiblock.IMultiElement;
import com.denfop.invslot.Inventory;

public interface IRecuperator extends IMultiElement {

    Inventory getExchanger();

    double getPower();

}
