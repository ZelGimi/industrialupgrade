package com.denfop.api.steam;

import com.denfop.api.multiblock.IMultiElement;
import com.denfop.invslot.InvSlot;
import com.denfop.tiles.reactors.graphite.IExchangerItem;

public interface IExchanger extends IMultiElement {

    InvSlot getSlot();

    double getPower();

    IExchangerItem getExchanger();
}
