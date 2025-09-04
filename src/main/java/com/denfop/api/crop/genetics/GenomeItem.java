package com.denfop.api.crop.genetics;


import net.minecraft.world.item.ItemStack;

public interface GenomeItem {

    GeneticTraits getGenomeTraits(ItemStack stack);
}
