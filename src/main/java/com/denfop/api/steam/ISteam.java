package com.denfop.api.steam;

import com.denfop.api.multiblock.IMainMultiBlock;
import com.denfop.tiles.mechanism.steamturbine.IRod;
import com.denfop.tiles.mechanism.steamturbine.ISocket;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import java.util.List;

public interface ISteam extends IMainMultiBlock {


    ISocket getEnergy();

    List<com.denfop.tiles.mechanism.steamturbine.ICoolant> getCoolant();

    List<com.denfop.tiles.mechanism.steamturbine.IExchanger> getExchanger();

    FluidTank getSteamFluid();

    FluidTank getWaterFluid();

    EnumSteamPhase getSteamPhase();

    int getPhase();

    void addPhase(int phase);

    void removePhase(int phase);

    void updateInfo();

    int getPressure();

    List<IRod> getInfo();

    boolean isWork();

    void setGeneration(double generation);

    double getHeat();

    void removeHeat(double heat);

    void addHeat(double heat);

    double getMaxHeat();

    EnumSteamPhase getStableSteamPhase();
}
