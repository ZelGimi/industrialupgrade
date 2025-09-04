package com.denfop.api.otherenergies.pressure;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public interface IPressureNet {

    IPressureTile getSubTile(Level var1, BlockPos var2);


}
