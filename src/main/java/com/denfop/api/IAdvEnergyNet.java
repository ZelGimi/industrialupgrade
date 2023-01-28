package com.denfop.api;

import com.denfop.api.energy.EnergyNetLocal;
import com.denfop.api.energy.SunCoef;
import ic2.api.energy.IEnergyNet;
import ic2.api.energy.tile.IEnergyTile;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public interface IAdvEnergyNet extends IEnergyNet {


    double getPowerFromTier(int var1);

    int getTierFromPower(double var1);

    double getRFFromEU(int amount);


    SunCoef getSunCoefficient(World world);

    boolean getTransformerMode();

    boolean getLosing();

    boolean needExplosion();

    boolean needIgnoringTiers();

    boolean hasRestrictions();

    TileEntity getBlockPosFromEnergyTile(IEnergyTile tile);

    List<EnergyNetLocal.EnergyPath> getEnergyPaths(World world, BlockPos pos);

    void update();


}
