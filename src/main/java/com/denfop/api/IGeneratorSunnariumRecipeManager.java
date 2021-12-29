package com.denfop.api;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Map;

public interface IGeneratorSunnariumRecipeManager {

    void addRecipe(NBTTagCompound var2, ItemStack var3);


    Map<NBTTagCompound, ItemStack> getRecipes();

}
