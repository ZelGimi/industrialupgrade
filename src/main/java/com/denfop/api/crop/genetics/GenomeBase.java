package com.denfop.api.crop.genetics;

import com.denfop.api.crop.Crop;
import net.minecraft.nbt.CompoundTag;

public interface GenomeBase {

    boolean hasGenome(EnumGenetic genome);

    <T> T getLevelGenome(final EnumGenetic genome, Class<T> tClass);

    GeneticTraits getGenome(final EnumGenetic genome);

    void loadCrop(Crop crop);

    Genome copy();

    CompoundTag writeNBT(CompoundTag nbtTagCompound);
}
