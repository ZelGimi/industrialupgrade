package com.denfop.blockentity.reactors.heat;

import com.denfop.api.multiblock.IMultiElement;
import com.denfop.componets.Fluids;
import net.minecraft.world.level.material.Fluid;

public interface ITank extends IMultiElement {

    Fluids getFluids();

    Fluids.InternalFluidTank getTank();

    void setFluid(Fluid fluid);

}
