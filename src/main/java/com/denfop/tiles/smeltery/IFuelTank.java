package com.denfop.tiles.smeltery;

import com.denfop.api.multiblock.IMultiElement;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public interface IFuelTank extends IMultiElement {

    FluidTank getFuelTank();

    double getSpeed();

}
