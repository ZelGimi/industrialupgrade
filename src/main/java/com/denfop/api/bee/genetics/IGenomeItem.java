package com.denfop.api.bee.genetics;


import net.minecraft.world.item.ItemStack;

public interface IGenomeItem {
    GeneticTraits getGenomeTraits(ItemStack stack);
}
