package com.denfop.api;

import com.denfop.api.energy.*;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.List;
import java.util.Map;

public interface IAdvEnergyNet {


    double getPowerFromTier(int var1);

    int getTierFromPower(double var1);

    SunCoef getSunCoefficient(Level world);

    boolean getTransformerMode();

    Map<ChunkPos, List<IEnergySink>> getChunkPosListMap(Level world);

    boolean getLosing();

    boolean needExplosion();

    boolean needIgnoringTiers();

    boolean hasRestrictions();

    BlockEntity getBlockPosFromEnergyTile(IEnergyTile tile);

    List<Path> getEnergyPaths(Level world, BlockPos pos);

    void update();

    IEnergyTile getTile(Level var1, BlockPos var2);


    Level getWorld(IEnergyTile var1);

    BlockPos getPos(IEnergyTile var1);

    NodeStats getNodeStats(IEnergyTile var1);

}
