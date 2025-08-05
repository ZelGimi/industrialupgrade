package com.denfop.tiles.gasturbine;

import com.denfop.api.multiblock.IMultiElement;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

public interface ITank extends IMultiElement {

    FluidTank getTank();

}
