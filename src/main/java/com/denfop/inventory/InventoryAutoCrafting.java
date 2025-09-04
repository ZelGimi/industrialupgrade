package com.denfop.inventory;

import com.denfop.blockentity.mechanism.BlockEntityAutoCrafter;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;

public class InventoryAutoCrafting extends CraftingContainer {

    private final BlockEntityAutoCrafter tile;

    public InventoryAutoCrafting(BlockEntityAutoCrafter tile) {
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
