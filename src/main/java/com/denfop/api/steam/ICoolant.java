package com.denfop.api.steam;

import com.denfop.api.multiblock.MultiBlockElement;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public interface ICoolant extends MultiBlockElement {

    FluidTank getCoolant();

    double getPower();

    int getPressure();
}
