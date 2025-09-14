package com.denfop.recipe.universalrecipe;

import com.denfop.recipe.IInputItemStack;
import com.denfop.register.Register;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.LinkedList;
import java.util.List;

public class SmelteryRecipe implements Recipe<CraftingInput> {

    private final String operation;
    private final List<IInputItemStack> inputs;
    private final List<IInputItemStack> outputs;

    public SmelteryRecipe(String operation, List<IInputItemStack> inputs, List<IInputItemStack> outputs) {
        this.operation = operation;
        this.inputs = inputs;
        this.outputs = outputs;
    }


    public String getOperation() {
        return operation;
    }

    public List<IInputItemStack> getInputs() {
        return inputs;
    }

    public List<IInputItemStack> getOutputs() {
        return outputs;
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




    @Override
    public RecipeSerializer<?> getSerializer() {
        return Register.RECIPE_SERIALIZER_SMELTERY_RECIPE.get();
    }

    @Override
    public RecipeType<?> getType() {
        return Register.SMELTERY_RECIPE.get();
    }
}
