package com.denfop.integration.jei;

import net.minecraft.world.item.ItemStack;

import java.util.List;

/**
 * Marker interface for IU JEI handlers that can expose all item alternatives
 * for every recipe input slot. This is required for tag / ore dictionary inputs,
 * where one logical ingredient may contain many possible ItemStack variants.
 */
public interface IJeiVariantRecipe {

    void setInputVariants(List<List<ItemStack>> inputVariants);

    List<ItemStack> getInputVariants(int slot, ItemStack fallback);

}
