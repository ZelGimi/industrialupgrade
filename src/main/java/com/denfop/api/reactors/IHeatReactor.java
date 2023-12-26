package com.denfop.api.reactors;

import com.denfop.api.multiblock.IMainMultiBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidTank;

public interface IHeatReactor extends IAdvReactor, IMainMultiBlock {

    FluidTank getWaterTank();

    FluidTank getOxygenTank();

    FluidTank getHydrogenTank();

    FluidTank getHeliumTank();

    int[] getLengthGraphiteIndex(int index);
    ItemStack getGraphite(int index);

    int getLevelGraphite(int index);

    double getFuelGraphite(int index);

    void consumeFuelGraphite(int index,double fuel);

    void consumeGraphite(int index);

    int getLengthPump();

    int getPowerPump(int i);

    int getEnergyPump(int i);

    void damagePump(int i);

    int getLengthSimplePump();

    int getPowerSimplePump(int i);

    int getEnergySimplePump(int i);

    void addHeliumToRegenerate(double col);

    double getHeliumToRegenerate();

    void updateDataReactor();
}
