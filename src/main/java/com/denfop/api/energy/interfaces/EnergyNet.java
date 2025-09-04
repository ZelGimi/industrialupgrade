package com.denfop.api.energy.interfaces;

import com.denfop.api.energy.SunCoef;
import com.denfop.api.energy.networking.NodeStats;
import com.denfop.api.energy.networking.Path;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.List;
import java.util.Map;

public interface EnergyNet {


    double getPowerFromTier(int var1);

    int getTierFromPower(double var1);

    SunCoef getSunCoefficient(Level world);

    boolean getTransformerMode();

    Map<ChunkPos, List<EnergySink>> getChunkPosListMap(Level world);

    boolean getLosing();

    boolean needExplosion();

    boolean needIgnoringTiers();

    boolean hasRestrictions();

    BlockEntity getBlockPosFromEnergyTile(EnergyTile tile, Level level);

    List<Path> getEnergyPaths(Level world, BlockPos pos);

    void update();

    EnergyTile getTile(Level var1, BlockPos var2);


    NodeStats getNodeStats(EnergyTile var1, Level level);

}
