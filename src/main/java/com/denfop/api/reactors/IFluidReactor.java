package com.denfop.api.reactors;

import com.denfop.api.multiblock.IMainMultiBlock;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

public interface IFluidReactor extends IAdvReactor, IMainMultiBlock {

    FluidTank getInputTank();

    FluidTank getCoolantTank();

    FluidTank getHotCoolantTank();

    FluidTank getOutputTank();

    int getPressure();

    void addPressure(int pressure);

    void removePressure(int pressure);


}
