package com.denfop.tiles.cyclotron;

import com.denfop.api.multiblock.IMultiElement;
import net.minecraftforge.fluids.FluidTank;

public interface ICoolant extends IMultiElement {

    FluidTank getCoolantTank();

}
