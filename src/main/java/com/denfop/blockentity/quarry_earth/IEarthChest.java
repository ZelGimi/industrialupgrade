package com.denfop.blockentity.quarry_earth;

import com.denfop.api.multiblock.IMultiElement;
import com.denfop.inventory.Inventory;

public interface IEarthChest extends IMultiElement {

    Inventory getSlot();

}
