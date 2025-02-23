package com.denfop.tiles.gaswell;

import com.denfop.api.multiblock.IMultiElement;
import com.denfop.componets.Fluids;
import net.minecraftforge.fluids.FluidTank;

public interface ITank extends IMultiElement {

    Fluids.InternalFluidTank getTank();
}
