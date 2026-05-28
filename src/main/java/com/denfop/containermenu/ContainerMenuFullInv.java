package com.denfop.containermenu;

import com.denfop.blockentity.base.BlockEntityInventory;
import net.minecraft.world.entity.player.Player;

public abstract class ContainerMenuFullInv<T extends BlockEntityInventory> extends ContainerMenuBase<T> {
    public ContainerMenuFullInv(Player player, T t, int height) {
        super(t, player);
        if (player != null)
            this.addPlayerInventorySlots(player.getInventory(), height);
    }

    public ContainerMenuFullInv(Player player, T base) {
        super(base, player);
        this.addPlayerInventorySlots(player.getInventory(), 166);
    }

    public ContainerMenuFullInv(T base, Player player) {
        super(base, player);
        this.addPlayerInventorySlots(player.getInventory(), 166);
    }

    public ContainerMenuFullInv(Player player, T t, int width, int height, boolean addSlots) {
        super(t, player);
        if (addSlots)
            this.addPlayerInventorySlots(player.getInventory(), width, height);
    }

    public ContainerMenuFullInv(Player player, T t, int width, int height) {
        this(player, t, width, height, true);
    }
}
