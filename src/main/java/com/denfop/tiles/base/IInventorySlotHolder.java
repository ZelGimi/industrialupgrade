package com.denfop.tiles.base;

import com.denfop.invslot.InvSlot;
import net.minecraft.inventory.IInventory;

public interface IInventorySlotHolder<P extends TileEntityInventory & IInventory> {

    P getParent();

    InvSlot getInventorySlot(String var1);

    void addInventorySlot(InvSlot var1);

    int getBaseIndex(InvSlot var1);

}
