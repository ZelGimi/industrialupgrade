package com.denfop.recipe.universalrecipe;

import com.denfop.recipe.IInputItemStack;
import com.denfop.register.Register;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.fluids.FluidStack;


import java.util.LinkedList;
import java.util.List;

public class SmelteryRecipe implements  Recipe<CraftingInput> {
    private final String recipeType;
    private final  List<FluidStack> inputs;
    private final ItemStack stack;

    public SmelteryRecipe(String recipeType, List<FluidStack> fluidStacks, ItemStack stack) {
        this.recipeType = recipeType;
        this.inputs = fluidStacks;
        this.stack = stack;
    }


    @Override
    public boolean matches(CraftingInput container, Level level) {
        return false;
    }

    @Override
    public ItemStack assemble(CraftingInput pContainer, HolderLookup.Provider pRegistryAccess) {
        return ItemStack.EMPTY;
    }



    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }



    @Override
    public ItemStack getResultItem(HolderLookup.Provider pRegistryAccess) {
        return ItemStack.EMPTY;
    }



    public  List<FluidStack> getInputs() {
        return inputs;
    }
    public  List<IInputItemStack> getInput() {
        return new LinkedList<>();
    }
    public  List<IInputItemStack> getOutput() {
        return new LinkedList<>();
    }
    public ItemStack getStack() {
        return stack;
    }

    public String getRecipeType() {
        return recipeType;
    }



    @Override
    public RecipeSerializer<?> getSerializer() {
        return Register.RECIPE_SERIALIZER_SMELTERY_RECIPE.get();
    }

    @Override
    public RecipeType<?> getType() {
        return Register.SMELTERY_RECIPE.get();
    }
}
