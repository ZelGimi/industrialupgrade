package com.denfop.api.otherenergies.common.interfaces;

import com.denfop.api.energy.networking.NodeStats;
import com.denfop.api.otherenergies.common.networking.Path;
import net.minecraft.core.BlockPos;

import java.util.List;

public interface LocalNet {

    void TickEnd();

    void addTile(Tile tile1);

    void onUnload();

    Tile getTileEntity(BlockPos pos);

    NodeStats getNodeStats(final Tile tile);

    void removeTile(Tile tile1);

    List<Path> getPaths(final Acceptor par1);

}
