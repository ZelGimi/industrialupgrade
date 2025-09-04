package com.denfop.api.otherenergies.common;

import com.denfop.api.energy.networking.NodeStats;
import net.minecraft.core.BlockPos;

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
