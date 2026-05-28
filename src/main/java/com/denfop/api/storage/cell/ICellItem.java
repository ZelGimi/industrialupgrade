package com.denfop.api.storage.cell;

import net.minecraft.world.item.ItemStack;

public interface ICellItem {
    CellInfo getCellInfo();

    ItemStackCell getCell(ItemStack itemStack);
}
