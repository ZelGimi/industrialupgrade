package com.denfop.recipemanager;

import com.denfop.api.IGeneratorSunnariumRecipeManager;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.HashMap;
import java.util.Map;

public class GeneratorSunnariumRecipeManager implements IGeneratorSunnariumRecipeManager {


    private final Map<NBTTagCompound, ItemStack> recipes = new HashMap<>();

    @Override
    public void addRecipe(NBTTagCompound var2, ItemStack var3) {
        if (var3 == null) {
            throw new NullPointerException("The recipe output is null");
        }
        this.recipes.put(var2, var3);
    }

    @Override
    public Map<NBTTagCompound, ItemStack> getRecipes() {
        return this.recipes;
    }

}
