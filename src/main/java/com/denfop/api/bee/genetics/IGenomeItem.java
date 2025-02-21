package com.denfop.api.bee.genetics;

import net.minecraft.item.ItemStack;

public interface IGenomeItem {
    GeneticTraits getGenomeTraits(ItemStack stack);
}
