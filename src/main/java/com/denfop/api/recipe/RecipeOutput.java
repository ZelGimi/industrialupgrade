package com.denfop.api.recipe;


import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;


public final class RecipeOutput {

    public final List<ItemStack> items;
    public final CompoundTag metadata;

    public RecipeOutput(CompoundTag metadata1, List<ItemStack> items1) {
        assert !items1.contains(ItemStack.EMPTY);

        this.metadata = metadata1;
        this.items = items1;
    }

    public RecipeOutput(CompoundTag metadata1, ItemStack... items1) {
        this(metadata1, Arrays.asList(items1));
    }

    public boolean equals(Object obj) {
        if (obj instanceof RecipeOutput ro) {
            if (this.items.size() == ro.items.size() && (this.metadata == null && ro.metadata == null || this.metadata != null && ro.metadata != null && this.metadata.equals(
                    ro.metadata))) {
                Iterator<ItemStack> itA = this.items.iterator();
                Iterator<ItemStack> itB = ro.items.iterator();

                while (itA.hasNext() && itB.hasNext()) {
                    ItemStack stackA = itA.next();
                    ItemStack stackB = itB.next();
                    if (ItemStack.isSame(stackA, stackB)) {
                        return false;
                    }
                }

                return true;
            }
        }

        return false;
    }


}
