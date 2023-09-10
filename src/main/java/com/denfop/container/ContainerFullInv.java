package com.denfop.container;

import com.denfop.tiles.base.TileEntityInventory;
import net.minecraft.entity.player.EntityPlayer;

public abstract class ContainerFullInv<T extends TileEntityInventory> extends ContainerBase<T> {

    public ContainerFullInv(EntityPlayer player, T base, int height) {
        super(base);
        this.addPlayerInventorySlots(player, height);
    }

    public ContainerFullInv(EntityPlayer player, T base, int width, int height) {
        super(base);
        this.addPlayerInventorySlots(player, width, height);
    }

}
