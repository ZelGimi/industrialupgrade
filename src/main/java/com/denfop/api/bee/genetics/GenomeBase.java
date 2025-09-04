package com.denfop.api.bee.genetics;

import net.minecraft.world.item.ItemStack;

public interface GenomeBase {

    boolean hasGenome(EnumGenetic genome);

    <T> T getLevelGenome(final EnumGenetic genome, Class<T> tClass);

    GeneticTraits getGenome(final EnumGenetic genome);

    Genome copy();

    void writeNBT(ItemStack nbtTagCompound);


}
