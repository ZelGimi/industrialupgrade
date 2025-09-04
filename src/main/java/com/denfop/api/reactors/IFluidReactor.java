package com.denfop.api.reactors;

import com.denfop.api.multiblock.MainMultiBlock;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public interface IFluidReactor extends IAdvReactor, MainMultiBlock {

    FluidTank getInputTank();

    FluidTank getCoolantTank();

    FluidTank getHotCoolantTank();

    FluidTank getOutputTank();

    int getPressure();

    void addPressure(int pressure);

    void removePressure(int pressure);


}
