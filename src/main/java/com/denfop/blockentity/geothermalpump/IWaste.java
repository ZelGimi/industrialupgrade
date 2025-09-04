package com.denfop.blockentity.geothermalpump;

import com.denfop.api.multiblock.MultiBlockElement;
import com.denfop.inventory.Inventory;

public interface IWaste extends MultiBlockElement {

    Inventory getSlot();

}
