package com.denfop.blockentity.quarry_earth;

import com.denfop.api.multiblock.MultiBlockElement;
import com.denfop.inventory.Inventory;

public interface IEarthChest extends MultiBlockElement {

    Inventory getSlot();

}
