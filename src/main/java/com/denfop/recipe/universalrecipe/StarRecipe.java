package com.denfop.recipe.universalrecipe;

import com.denfop.recipe.IInputItemStack;
import com.denfop.recipe.InputItemStack;
import com.denfop.register.Register;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class StarRecipe implements Recipe<CraftingInput> {


    public String name;
    public String systemName;
    public String texturePath;
    public Integer angle;
    public Double size;

    public StarRecipe(String name, String systemName, String texturePath, Integer angle, Double size) {
        this.name = name;
        this.systemName = systemName;
        this.texturePath = texturePath;
        this.angle = angle;
        this.size = size;
    }

    @Override
    public boolean matches(CraftingInput inv, Level world) {
        return false;
    }

    @Override
    public ItemStack assemble(CraftingInput container, HolderLookup.Provider provider) {
        return ItemStack.EMPTY;
    }


    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return ItemStack.EMPTY;
    }




    @Override
    public RecipeSerializer<?> getSerializer() {
        return Register.RECIPE_SERIALIZER_STAR_RECIPE.get();
    }

    @Override
    public RecipeType<?> getType() {
        return Register.STAR_RECIPE.get();
    }


}
