package com.denfop.api.steam;

import com.denfop.api.multiblock.MultiBlockElement;
import com.denfop.blockentity.reactors.graphite.IExchangerItem;
import com.denfop.inventory.Inventory;

public interface IExchanger extends MultiBlockElement {

    Inventory getSlot();

    double getPower();

    IExchangerItem getExchanger();
}
