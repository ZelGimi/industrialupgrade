package com.denfop.api.crop.genetics;


import net.minecraft.world.item.ItemStack;

public interface IGenomeItem {

    GeneticTraits getGenomeTraits(ItemStack stack);
}
