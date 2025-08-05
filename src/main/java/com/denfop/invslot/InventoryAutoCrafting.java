package com.denfop.invslot;

import com.denfop.tiles.mechanism.TileEntityAutoCrafter;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.TransientCraftingContainer;
import net.minecraft.world.item.ItemStack;

public class InventoryAutoCrafting extends TransientCraftingContainer {

    private final TileEntityAutoCrafter tile;

    public InventoryAutoCrafting(TileEntityAutoCrafter tile) {
        super((AbstractContainerMenu) null, 3, 3);
        this.tile = tile;
    }

    @Override
    public ItemStack getItem(int index) {
        return tile.getAutoCrafter().get(index);
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < this.getContainerSize(); i++) {
            if (!this.getItem(i).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        tile.getAutoCrafter().set(index, stack);
    }

    @Override
    public int getContainerSize() {
        return 9;
    }


}
