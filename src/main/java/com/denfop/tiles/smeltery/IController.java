package com.denfop.tiles.smeltery;

import com.denfop.api.multiblock.IMainMultiBlock;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

public interface IController extends IMainMultiBlock {

    FluidTank getFirstTank();

}
