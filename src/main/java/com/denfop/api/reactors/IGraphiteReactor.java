package com.denfop.api.reactors;

import com.denfop.api.multiblock.IMainMultiBlock;
import com.denfop.componets.AdvEnergy;
import com.denfop.componets.CoolComponent;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidTank;

public interface IGraphiteReactor extends IAdvReactor, IMainMultiBlock {

    FluidTank getWaterTank();

    FluidTank getCoalDioxideTank();

    ItemStack getGraphite(int index);

    boolean canWorkWithGraphite();
    int getLevelGraphite(int index);

    double getFuelGraphite(int index);

    void consumeFuelGraphite(int index,double fuel);

    void consumeGraphite(int index);

    FluidTank getSteamTank();

    FluidTank getOxideTank();


    double workCoolant(double heat);

    void updateDataReactor();

    AdvEnergy getEnergy();
}
