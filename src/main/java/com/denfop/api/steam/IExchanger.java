package com.denfop.api.steam;

import com.denfop.api.multiblock.IMultiElement;
import com.denfop.blockentity.reactors.graphite.IExchangerItem;
import com.denfop.inventory.Inventory;

public interface IExchanger extends IMultiElement {

    Inventory getSlot();

    double getPower();

    IExchangerItem getExchanger();
}
