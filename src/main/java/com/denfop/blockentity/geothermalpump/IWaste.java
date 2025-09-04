package com.denfop.blockentity.geothermalpump;

import com.denfop.api.multiblock.IMultiElement;
import com.denfop.inventory.Inventory;

public interface IWaste extends IMultiElement {

    Inventory getSlot();

}
