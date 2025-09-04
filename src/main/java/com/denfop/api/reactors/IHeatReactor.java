package com.denfop.api.reactors;

import com.denfop.api.multiblock.MainMultiBlock;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public interface IHeatReactor extends IAdvReactor, MainMultiBlock {

    FluidTank getWaterTank();

    FluidTank getOxygenTank();

    FluidTank getHydrogenTank();

    FluidTank getHeliumTank();

    int[] getLengthGraphiteIndex(int index);

    ItemStack getGraphite(int index);

    int getLevelGraphite(int index);

    double getFuelGraphite(int index);

    void consumeFuelGraphite(int index, double fuel);

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
