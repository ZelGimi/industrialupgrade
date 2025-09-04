package com.denfop.blockentity.smeltery;

import com.denfop.api.multiblock.IMultiElement;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

public interface IFuelTank extends IMultiElement {

    FluidTank getFuelTank();

    double getSpeed();

}
