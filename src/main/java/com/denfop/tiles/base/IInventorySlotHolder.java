package com.denfop.tiles.base;

import com.denfop.invslot.InvSlot;
import ic2.core.block.TileEntityBlock;
import net.minecraft.inventory.IInventory;

public interface IInventorySlotHolder<P extends TileEntityBlock & IInventory> {

    P getParent();

    InvSlot getInventorySlot(String var1);

    void addInventorySlot(InvSlot var1);

    int getBaseIndex(InvSlot var1);

}
