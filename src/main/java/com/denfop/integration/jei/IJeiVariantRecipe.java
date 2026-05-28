package com.denfop.integration.jei;

import net.minecraft.world.item.ItemStack;

import java.util.List;

/**
 * Exposes all JEI alternatives for item input slots. Required for tag / ore-dict
 * based inputs, because one logical recipe slot can accept many ItemStacks.
 */
public interface IJeiVariantRecipe {

    void setInputVariants(List<List<ItemStack>> inputVariants);

    List<ItemStack> getInputVariants(int slot, ItemStack fallback);

}
