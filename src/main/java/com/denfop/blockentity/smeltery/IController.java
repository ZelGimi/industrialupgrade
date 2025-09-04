package com.denfop.blockentity.smeltery;

import com.denfop.api.multiblock.MainMultiBlock;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public interface IController extends MainMultiBlock {

    FluidTank getFirstTank();

}
