package com.denfop.blockentity.gasturbine;

import com.denfop.api.multiblock.MultiBlockElement;
import com.denfop.inventory.Inventory;

public interface IRecuperator extends MultiBlockElement {

    Inventory getExchanger();

    double getPower();

}
