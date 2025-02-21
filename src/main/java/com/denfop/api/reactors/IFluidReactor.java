package com.denfop.api.reactors;

import com.denfop.api.multiblock.IMainMultiBlock;
import net.minecraftforge.fluids.FluidTank;

public interface IFluidReactor extends IAdvReactor, IMainMultiBlock {

    FluidTank getInputTank();

    FluidTank getCoolantTank();

    FluidTank getHotCoolantTank();

    FluidTank getOutputTank();

    int getPressure();

    void addPressure(int pressure);

    void removePressure(int pressure);


}
