package com.denfop.api.agriculture.genetics;

import com.denfop.api.agriculture.ICrop;
import net.minecraft.world.item.ItemStack;

public interface IGenome {

    boolean hasGenome(EnumGenetic genome);

    <T> T getLevelGenome(final EnumGenetic genome, Class<T> tClass);

    GeneticTraits getGenome(final EnumGenetic genome);

    void loadCrop(ICrop crop);

    Genome copy();

    void writeNBT(ItemStack stack);
}
