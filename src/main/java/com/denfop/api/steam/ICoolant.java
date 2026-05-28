package com.denfop.api.steam;

import com.denfop.api.multiblock.IMultiElement;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public interface ICoolant extends IMultiElement {

    FluidTank getCoolant();

    double getPower();

    int getPressure();
}
