package com.denfop.api.heat;


import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public interface IHeatNet {

    IHeatTile getSubTile(Level var1, BlockPos var2);


}
