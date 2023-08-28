package com.denfop.api.heat;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IHeatNet {

    IHeatTile getSubTile(World var1, BlockPos var2);


}
