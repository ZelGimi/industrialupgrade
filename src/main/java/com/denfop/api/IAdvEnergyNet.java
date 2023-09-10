package com.denfop.api;

import com.denfop.api.energy.IEnergyTile;
import com.denfop.api.energy.NodeStats;
import com.denfop.api.energy.Path;
import com.denfop.api.energy.SunCoef;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public interface IAdvEnergyNet {


    double getPowerFromTier(int var1);

    int getTierFromPower(double var1);

    SunCoef getSunCoefficient(World world);

    boolean getTransformerMode();

    boolean getLosing();

    boolean needExplosion();

    boolean needIgnoringTiers();

    boolean hasRestrictions();

    TileEntity getBlockPosFromEnergyTile(IEnergyTile tile);

    List<Path> getEnergyPaths(World world, BlockPos pos);

    void update();

    IEnergyTile getTile(World var1, BlockPos var2);


    World getWorld(IEnergyTile var1);

    BlockPos getPos(IEnergyTile var1);

    NodeStats getNodeStats(IEnergyTile var1);

}
