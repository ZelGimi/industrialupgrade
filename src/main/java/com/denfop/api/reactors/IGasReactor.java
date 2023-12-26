package com.denfop.api.reactors;

import com.denfop.api.multiblock.IMainMultiBlock;
import com.denfop.componets.AdvEnergy;
import net.minecraftforge.fluids.FluidTank;

public interface IGasReactor extends IAdvReactor, IMainMultiBlock {

    int getTemperatureRefrigerator();

    FluidTank getHeliumTank();

    FluidTank getWaterTank(int i);

    AdvEnergy getEnergy();

    FluidTank getHydrogenTank(int i);

    FluidTank getOxygenTank(int i);

    int getPressure(int i);

    int getLengthCompressors();

    int getEnergyCompressor(int i);

    void addHeliumToRegenerate(int col);

    int getCapacityHelium();

    int getHeliumToRegenerate();

    int getEnergyFan(int i);

    int getPowerFan(int i);

    void damageFan(int i);

    int getLengthFan();

    boolean hasFan();

    boolean hasPump();

    int getLengthPump();

    int getPowerPump(int i);

    int getEnergyPump(int i);

    void damagePump(int i);


}
