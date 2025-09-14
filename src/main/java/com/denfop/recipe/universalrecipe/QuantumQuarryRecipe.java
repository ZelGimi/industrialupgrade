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

public class QuantumQuarryRecipe implements Recipe<CraftingInput> {



    private final String recipeType;
    private final String recipeOperation;
    private final List<IInputItemStack> inputs;

    public QuantumQuarryRecipe(String recipeType, String recipeOperation, List<IInputItemStack> inputs) {
        this.recipeType = recipeType;
        this.recipeOperation = recipeOperation;
        this.inputs = inputs;
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

        return inputs;
    }

    public String getTypeOperation() {
        return recipeOperation;
    }


    public String getRecipeType() {
        return recipeType;
    }


    @Override
    public RecipeSerializer<?> getSerializer() {
        return Register.RECIPE_SERIALIZER_QUANTUM_QUARRY.get();
    }

    @Override
    public RecipeType<?> getType() {
        return Register.QUANTUM_QUARRY.get();
    }
}
