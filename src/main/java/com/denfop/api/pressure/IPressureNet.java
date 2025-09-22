package com.denfop.api.pressure;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IPressureNet {

    IPressureTile getSubTile(World var1, BlockPos var2);


}
