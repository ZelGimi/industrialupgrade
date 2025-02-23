package com.denfop.tiles.smeltery;

import com.denfop.api.multiblock.IMainMultiBlock;
import net.minecraftforge.fluids.FluidTank;

public interface IController extends IMainMultiBlock {

    FluidTank getFirstTank();

}
