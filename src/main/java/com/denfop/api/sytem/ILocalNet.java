package com.denfop.api.sytem;

import com.denfop.api.energy.NodeStats;
import net.minecraft.util.math.BlockPos;

public interface ILocalNet {

    void TickEnd();

    void addTile(ITile tile1);

    void onUnload();

    ITile getTileEntity(BlockPos pos);

    void update(BlockPos pos);

    NodeStats getNodeStats(final ITile tile);

    void removeTile(ITile tile1);

}
