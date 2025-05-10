package com.denfop.datagen.blocktags;

import net.minecraft.world.level.block.Block;
import oshi.util.tuples.Pair;

public interface IBlockTag {

    Block getBlock();

    Pair<String, Integer> getHarvestLevel();
}
