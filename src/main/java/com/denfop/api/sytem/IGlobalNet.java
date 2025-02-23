package com.denfop.api.sytem;

import com.denfop.api.energy.NodeStats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Map;

public interface IGlobalNet {

    ITile getSubTile(World var1, BlockPos var2);

    void addTile(World var1, ITile var2);

    void removeTile(World var1, ITile var2);

    ILocalNet getLocalSystem(World world);

    ILocalNet getLocalSystem(int id);

    void onUnload(int id);

    Map<Integer, ILocalNet> getLocalNetMap();

    void TickEnd(int id);

    NodeStats getNodeStats(ITile delegate, World world);

}
