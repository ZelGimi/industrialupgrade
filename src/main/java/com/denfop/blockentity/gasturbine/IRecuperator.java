package com.denfop.blockentity.gasturbine;

import com.denfop.api.multiblock.IMultiElement;
import com.denfop.inventory.Inventory;

public interface IRecuperator extends IMultiElement {

    Inventory getExchanger();

    double getPower();

}
