package com.denfop.blockentity.smeltery;

import com.denfop.api.multiblock.MultiBlockElement;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public interface IFuelTank extends MultiBlockElement {

    FluidTank getFuelTank();

    double getSpeed();

}
