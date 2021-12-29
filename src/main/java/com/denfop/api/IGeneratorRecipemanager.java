package com.denfop.api;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;
import java.util.Map;

public interface IGeneratorRecipemanager {

    void addRecipe(NBTTagCompound var2, FluidStack var3);

    List getOutputFor(boolean var2);

    Map<NBTTagCompound, FluidStack> getRecipes();

}
