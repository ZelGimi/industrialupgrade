package com.denfop.recipe.universalrecipe;

import com.denfop.register.Register;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public class SmelteryRecipe implements Recipe<Container> {
    private final ResourceLocation id;
    private final String recipeType;
    private final List<FluidStack> inputs;
    private final ItemStack stack;

    public SmelteryRecipe(ResourceLocation id, String recipeType, List<FluidStack> fluidStacks, ItemStack stack) {
        this.id = id;
        this.recipeType = recipeType;
        this.inputs = fluidStacks;
        this.stack = stack;
    }

    @Override
    public boolean matches(Container inv, Level world) {
        return false;
    }

    @Override
    public ItemStack assemble(Container pContainer) {
        return ItemStack.EMPTY;
    }


    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem() {
        return ItemStack.EMPTY;
    }


    @Override
    public ResourceLocation getId() {
        return id;
    }

    public List<FluidStack> getInputs() {
        return inputs;
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
