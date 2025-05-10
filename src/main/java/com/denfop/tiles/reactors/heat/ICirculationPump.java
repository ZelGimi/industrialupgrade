package com.denfop.tiles.reactors.heat;

import com.denfop.api.multiblock.IMultiElement;
import com.denfop.invslot.InvSlot;

public interface ICirculationPump extends IMultiElement {

    int getEnergy();

    int getPower();

    InvSlot getSlot();

}
