package com.denfop.tiles.reactors.gas;

import com.denfop.api.multiblock.IMultiElement;
import com.denfop.invslot.Inventory;

public interface IInterCooler extends IMultiElement {

    int getEnergy();

    int getPower();

    Inventory getSlot();

}
