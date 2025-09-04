package com.denfop.blockentity.reactors.heat;

import com.denfop.api.multiblock.MultiBlockElement;
import com.denfop.inventory.Inventory;

public interface ICirculationPump extends MultiBlockElement {

    int getEnergy();

    int getPower();

    Inventory getSlot();

}
