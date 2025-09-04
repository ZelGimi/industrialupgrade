package com.denfop.blockentity.cyclotron;

import com.denfop.api.multiblock.IMultiElement;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;

public interface ICryogen extends IMultiElement {

    FluidTank getCryogenTank();

}
