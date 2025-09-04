package com.denfop.blockentity.cyclotron;

import com.denfop.api.multiblock.MultiBlockElement;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public interface ICryogen extends MultiBlockElement {

    FluidTank getCryogenTank();

}
