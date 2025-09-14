package com.denfop.recipe.universalrecipe;

import com.denfop.recipe.IInputItemStack;
import com.denfop.recipe.InputItemStack;
import com.denfop.register.Register;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class ColonyRecipe implements Recipe<CraftingInput> {


    final String bodyName;
    final List<IInputItemStack> input;
    final int level;

    public ColonyRecipe(String bodyName, List<IInputItemStack> input, int level) {
        this.bodyName = bodyName;
        this.input = input;
        this.level = level;
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


    public List<IInputItemStack> getInputs() {
        return input;
    }



    @Override
    public RecipeSerializer<?> getSerializer() {
        return Register.RECIPE_SERIALIZER_COLONY_RECIPE.get();
    }

    @Override
    public RecipeType<?> getType() {
        return Register.COLONY_RECIPE.get();
    }

}
