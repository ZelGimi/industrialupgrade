package com.denfop.container;

import com.denfop.tiles.base.TileEntityInventory;
import net.minecraft.entity.player.EntityPlayer;

public abstract class ContainerFullInv<T extends TileEntityInventory> extends ContainerBase<T> {

    public ContainerFullInv(EntityPlayer player, T base, int height) {
        super(base);
        this.addPlayerInventorySlots(player, height);
    }

    public ContainerFullInv(EntityPlayer player, T base) {
        super(base);
        this.addPlayerInventorySlots(player, 166);
    }

    public ContainerFullInv(T base, EntityPlayer player) {
        super(base);
        this.addPlayerInventorySlots(player, 166);
    }

    public ContainerFullInv(EntityPlayer player, T base, int width, int height) {
        super(base);
        this.addPlayerInventorySlots(player, width, height);
    }

}
