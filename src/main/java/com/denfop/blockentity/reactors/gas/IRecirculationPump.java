package com.denfop.blockentity.reactors.gas;

import com.denfop.api.multiblock.MultiBlockElement;
import com.denfop.inventory.Inventory;

public interface IRecirculationPump extends MultiBlockElement {

    int getEnergy();

    int getPower();

    Inventory getSlot();

}
