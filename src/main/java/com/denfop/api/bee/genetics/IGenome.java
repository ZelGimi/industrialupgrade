package com.denfop.api.bee.genetics;

import com.denfop.api.agriculture.ICrop;
import net.minecraft.nbt.NBTTagCompound;

public interface IGenome {

    boolean hasGenome(EnumGenetic genome);

    <T> T getLevelGenome(final EnumGenetic genome, Class<T> tClass);

    GeneticTraits getGenome(final EnumGenetic genome);

    Genome copy();

    NBTTagCompound writeNBT(NBTTagCompound nbtTagCompound);
}
