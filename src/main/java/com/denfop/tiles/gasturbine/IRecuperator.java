package com.denfop.tiles.gasturbine;

import com.denfop.api.multiblock.IMultiElement;
import com.denfop.invslot.InvSlot;
import com.denfop.tiles.reactors.graphite.IExchangerItem;

public interface IRecuperator  extends IMultiElement {

    InvSlot getExchanger();

    double getPower();
}
