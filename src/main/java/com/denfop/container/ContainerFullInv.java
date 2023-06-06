package com.denfop.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;

public abstract class ContainerFullInv<T extends IInventory> extends ContainerBase<T> {

    public ContainerFullInv(EntityPlayer player, T base, int height) {
        super(base);
        this.addPlayerInventorySlots(player, height);
    }

    public ContainerFullInv(EntityPlayer player, T base, int width, int height) {
        super(base);
        this.addPlayerInventorySlots(player, width, height);
    }

}
