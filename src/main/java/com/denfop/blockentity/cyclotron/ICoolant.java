package com.denfop.blockentity.cyclotron;

import com.denfop.api.multiblock.MultiBlockElement;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public interface ICoolant extends MultiBlockElement {

    FluidTank getCoolantTank();

}
