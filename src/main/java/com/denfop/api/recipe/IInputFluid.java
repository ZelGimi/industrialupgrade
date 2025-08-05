package com.denfop.api.recipe;


import com.denfop.recipe.IInputItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public interface IInputFluid {

    List<FluidStack> getInputs();

    IInputItemStack getStack();
    CompoundTag writeNBT();
}
