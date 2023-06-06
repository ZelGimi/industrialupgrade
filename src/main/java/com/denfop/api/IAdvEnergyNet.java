package com.denfop.api;

import com.denfop.api.energy.EnergyNetLocal;
import com.denfop.api.energy.IAdvEnergyTile;
import com.denfop.api.energy.NodeStats;
import com.denfop.api.energy.SunCoef;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public interface IAdvEnergyNet {


    double getPowerFromTier(int var1);

    int getTierFromPower(double var1);

    double getRFFromEU(int amount);

    EnergyNetLocal getEnergyLocal(World world);

    SunCoef getSunCoefficient(World world);

    boolean getTransformerMode();

    boolean getLosing();

    boolean needExplosion();

    boolean needIgnoringTiers();

    boolean hasRestrictions();

    TileEntity getBlockPosFromEnergyTile(IAdvEnergyTile tile);

    List<EnergyNetLocal.EnergyPath> getEnergyPaths(World world, BlockPos pos);

    void update();

    IAdvEnergyTile getTile(World var1, BlockPos var2);

    IAdvEnergyTile getSubTile(World var1, BlockPos var2);


    World getWorld(IAdvEnergyTile var1);

    BlockPos getPos(IAdvEnergyTile var1);

    NodeStats getNodeStats(IAdvEnergyTile var1);

}
