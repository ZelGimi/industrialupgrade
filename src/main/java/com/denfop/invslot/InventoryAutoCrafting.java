package com.denfop.invslot;

import com.denfop.tiles.mechanism.TileEntityAutoCrafter;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;

public class InventoryAutoCrafting extends InventoryCrafting {

    private final TileEntityAutoCrafter tile;

    public InventoryAutoCrafting(final TileEntityAutoCrafter tile) {
        super(null, 3, 3);
        this.tile = tile;
    }

    @Override
    public ItemStack getStackInSlot(final int index) {
        return tile.getAutoCrafter().get(index);
    }

    @Override
    public boolean isEmpty() {
        return tile.getAutoCrafter().isEmpty();
    }

}
