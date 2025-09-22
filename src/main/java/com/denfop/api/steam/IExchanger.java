package com.denfop.api.steam;

import com.denfop.api.multiblock.IMultiElement;
import com.denfop.invslot.Inventory;
import com.denfop.tiles.reactors.graphite.IExchangerItem;

public interface IExchanger extends IMultiElement {

    Inventory getSlot();

    double getPower();

    IExchangerItem getExchanger();

}
