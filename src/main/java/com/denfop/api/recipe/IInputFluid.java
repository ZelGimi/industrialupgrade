package com.denfop.api.recipe;

import com.denfop.recipe.IInputItemStack;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public interface IInputFluid {

    List<FluidStack> getInputs();

    IInputItemStack getStack();

}
