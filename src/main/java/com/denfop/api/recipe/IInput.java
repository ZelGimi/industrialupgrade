package com.denfop.api.recipe;

import com.denfop.recipe.IInputItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public interface IInput {

    List<IInputItemStack> getInputs();

    CompoundTag writeNBT();
    boolean hasFluids();

    FluidStack getFluid();

    List<FluidStack> getFluidInputs();

    List<ItemStack> getStackInputs();

    List<ItemStack> getAllStackInputs();
}
