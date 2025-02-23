package com.denfop.tiles.gasturbine;

import com.denfop.api.multiblock.IMultiElement;
import net.minecraftforge.fluids.FluidTank;

public interface ITank  extends IMultiElement {

    FluidTank getTank();
}
