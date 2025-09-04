package com.denfop.api.otherenergies.common;

import com.denfop.api.energy.networking.NodeStats;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import java.util.Map;

public interface IGlobalNet {

    ITile getSubTile(Level var1, BlockPos var2);

    void addTile(Level var1, ITile var2);

    void removeTile(Level var1, ITile var2);

    ILocalNet getLocalSystem(Level world);

    ILocalNet getLocalSystem(ResourceKey<Level> id);

    void onUnload(ResourceKey<Level> id);

    Map<ResourceKey<Level>, ILocalNet> getLocalNetMap();

    void TickEnd(ResourceKey<Level> id);

    NodeStats getNodeStats(ITile delegate, Level world);

}
