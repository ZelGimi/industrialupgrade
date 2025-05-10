package com.denfop.tiles.cyclotron;

import com.denfop.api.multiblock.IMultiElement;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public interface ICoolant extends IMultiElement {

    FluidTank getCoolantTank();

}
