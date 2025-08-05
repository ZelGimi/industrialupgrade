package com.denfop.api.reactors;

import com.denfop.api.multiblock.IMainMultiBlock;
import com.denfop.componets.Energy;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

public interface IGraphiteReactor extends IAdvReactor, IMainMultiBlock {

    FluidTank getWaterTank();

    FluidTank getCoalDioxideTank();

    ItemStack getGraphite(int index);

    boolean canWorkWithGraphite();

    int getLevelGraphite(int index);

    double getFuelGraphite(int index);

    void consumeFuelGraphite(int index, double fuel);

    void consumeGraphite(int index);

    FluidTank getSteamTank();

    FluidTank getOxideTank();


    double workCoolant(double heat);

    void updateDataReactor();

    Energy getEnergy();

}
