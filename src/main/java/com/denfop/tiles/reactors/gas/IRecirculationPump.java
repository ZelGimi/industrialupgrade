package com.denfop.tiles.reactors.gas;

import com.denfop.api.multiblock.IMultiElement;
import com.denfop.invslot.Inventory;

public interface IRecirculationPump extends IMultiElement {

    int getEnergy();

    int getPower();

    Inventory getSlot();

}
