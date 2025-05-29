package com.denfop.api.agriculture.genetics;


import net.minecraft.world.item.ItemStack;

public interface IGenomeItem {

    GeneticTraits getGenomeTraits(ItemStack stack);
}
