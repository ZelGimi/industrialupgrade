package com.denfop.tiles.base;

import ic2.core.block.TileEntityBlock;
import net.minecraft.item.ItemStack;

public interface IIsMolecular {

    int getMode();

    ItemStack getItemStack();

    TileEntityBlock getEntityBlock();

}
