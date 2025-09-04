package com.denfop.blockentity.smeltery;

import com.denfop.api.multiblock.IMultiElement;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public interface ITank extends IMultiElement {


    FluidTank getTank();

}
