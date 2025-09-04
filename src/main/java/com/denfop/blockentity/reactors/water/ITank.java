package com.denfop.blockentity.reactors.water;

import com.denfop.api.multiblock.MultiBlockElement;
import com.denfop.componets.Fluids;
import net.minecraft.world.level.material.Fluid;

public interface ITank extends MultiBlockElement {

    Fluids getFluids();

    Fluids.InternalFluidTank getTank();

    void setFluid(Fluid fluid);

}
