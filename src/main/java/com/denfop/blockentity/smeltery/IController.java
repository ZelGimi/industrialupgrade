package com.denfop.blockentity.smeltery;

import com.denfop.api.multiblock.IMainMultiBlock;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public interface IController extends IMainMultiBlock {

    FluidTank getFirstTank();

}
