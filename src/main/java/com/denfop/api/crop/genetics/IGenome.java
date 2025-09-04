package com.denfop.api.crop.genetics;

import com.denfop.api.crop.ICrop;
import net.minecraft.world.item.ItemStack;

public interface IGenome {

    boolean hasGenome(EnumGenetic genome);

    <T> T getLevelGenome(final EnumGenetic genome, Class<T> tClass);

    GeneticTraits getGenome(final EnumGenetic genome);

    void loadCrop(ICrop crop);

    Genome copy();

    void writeNBT(ItemStack stack);
}
