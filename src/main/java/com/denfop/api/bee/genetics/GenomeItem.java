package com.denfop.api.bee.genetics;


import net.minecraft.world.item.ItemStack;

public interface GenomeItem {
    GeneticTraits getGenomeTraits(ItemStack stack);
}
