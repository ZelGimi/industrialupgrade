package com.denfop.blockentity.reactors.gas;

import com.denfop.api.multiblock.IMultiElement;
import com.denfop.inventory.Inventory;

public interface IInterCooler extends IMultiElement {

    int getEnergy();

    int getPower();

    Inventory getSlot();

}
