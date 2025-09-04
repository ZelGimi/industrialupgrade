package com.denfop.blockentity.gasturbine;

import com.denfop.api.multiblock.IMultiElement;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public interface ITank extends IMultiElement {

    FluidTank getTank();

}
