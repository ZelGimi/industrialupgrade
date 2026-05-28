package com.denfop.integration.jei;

import net.minecraft.world.item.ItemStack;

import java.util.List;

public interface IJeiVariantRecipe {

    void setInputVariants(List<List<ItemStack>> inputVariants);

    List<ItemStack> getInputVariants(int slot, ItemStack fallback);

}
