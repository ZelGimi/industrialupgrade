package com.denfop.recipemanager;

import com.denfop.api.IGeneratorRecipemanager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeneratorRecipeManager implements IGeneratorRecipemanager {

    private final Map<NBTTagCompound, FluidStack> recipes = new HashMap<>();

    @Override
    public void addRecipe(NBTTagCompound var2, FluidStack output) {

        if (output == null) {
            throw new NullPointerException("The recipe output is null");
        }

        this.recipes.put(var2, output);
    }

    @Override
    public List getOutputFor(boolean var2) {


        for (Map.Entry<NBTTagCompound, FluidStack> entry : this.recipes.entrySet()) {
            NBTTagCompound recipeInput = entry.getKey();
            if (recipeInput.getInteger("amount") > 0) {
                List list = new ArrayList();
                list.add(recipeInput.getInteger("amount"));
                list.add(entry.getValue());
                return list;
            }
        }
        return null;
    }

    @Override
    public Map<NBTTagCompound, FluidStack> getRecipes() {
        return this.recipes;
    }

}
