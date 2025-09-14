package com.denfop.api.recipe;

import com.denfop.recipe.IInputItemStack;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.List;

public interface IInput {

    List<IInputItemStack> getInputs();

    boolean hasFluids();

    FluidStack getFluid();

    List<FluidStack> getFluidInputs();

    List<ItemStack> getStackInputs();

    List<ItemStack> getAllStackInputs();

    CompoundTag writeNBT(RegistryAccess access);
}
