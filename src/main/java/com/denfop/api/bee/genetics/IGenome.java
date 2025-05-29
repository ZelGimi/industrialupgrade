package com.denfop.api.bee.genetics;

import net.minecraft.nbt.CompoundTag;

public interface IGenome {

    boolean hasGenome(EnumGenetic genome);

    <T> T getLevelGenome(final EnumGenetic genome, Class<T> tClass);

    GeneticTraits getGenome(final EnumGenetic genome);

    Genome copy();

    CompoundTag writeNBT(CompoundTag nbtTagCompound);
}
