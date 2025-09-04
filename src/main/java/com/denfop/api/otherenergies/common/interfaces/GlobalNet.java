package com.denfop.api.otherenergies.common.interfaces;

import com.denfop.api.energy.networking.NodeStats;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import java.util.Map;

public interface GlobalNet {

    Tile getSubTile(Level var1, BlockPos var2);

    void addTile(Level var1, Tile var2);

    void removeTile(Level var1, Tile var2);

    LocalNet getLocalSystem(Level world);

    LocalNet getLocalSystem(ResourceKey<Level> id);

    void onUnload(ResourceKey<Level> id);

    Map<ResourceKey<Level>, LocalNet> getLocalNetMap();

    void TickEnd(ResourceKey<Level> id);

    NodeStats getNodeStats(Tile delegate, Level world);

}
