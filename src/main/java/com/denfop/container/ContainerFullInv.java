package com.denfop.container;

import com.denfop.tiles.base.TileEntityInventory;
import net.minecraft.world.entity.player.Player;

public abstract class ContainerFullInv<T extends TileEntityInventory> extends ContainerBase<T> {
    public ContainerFullInv(Player player, T t, int height) {
        super(t, player);
        if (player != null)
            this.addPlayerInventorySlots(player.getInventory(), height);
    }

    public ContainerFullInv(Player player, T base) {
        super(base, player);
        this.addPlayerInventorySlots(player.getInventory(), 166);
    }

    public ContainerFullInv(T base, Player player) {
        super(base, player);
        this.addPlayerInventorySlots(player.getInventory(), 166);
    }

    public ContainerFullInv(Player player, T t, int width, int height) {
        super(t, player);
        this.addPlayerInventorySlots(player.getInventory(), width, height);
    }
}
