package com.denfop.api.recipe;


import com.denfop.recipe.IInputItemStack;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.List;

public interface IInputFluid {

    List<FluidStack> getInputs();

    IInputItemStack getStack();

    CompoundTag writeNBT(RegistryAccess registryAccess);
}
