package com.denfop.api.sytem;

import com.denfop.api.energy.NodeStats;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public interface ILocalNet {

    void TickEnd();

    void addTile(ITile tile1);

    void onUnload();

    ITile getTileEntity(BlockPos pos);

    NodeStats getNodeStats(final ITile tile);

    void removeTile(ITile tile1);

    List<Path> getPaths(final IAcceptor par1);

}
