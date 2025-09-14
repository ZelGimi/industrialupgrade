package com.denfop.recipe.universalrecipe;

import com.denfop.recipe.IInputItemStack;
import com.denfop.register.Register;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.List;

public class QuantumQuarryRecipe implements Recipe<Container> {
    private final ResourceLocation id;


    private final String recipeType;
    private final String recipeOperation;
    private final List<ItemStack> inputs;

    public QuantumQuarryRecipe(ResourceLocation id, String recipeType, String recipeOperation, List<ItemStack> input) {
        this.id = id;
        this.recipeType = recipeType;
        this.recipeOperation = recipeOperation;
        this.inputs = input;
    }

    @Override
    public boolean matches(Container inv, Level world) {
        return false;
    }

    @Override
    public ItemStack assemble(Container pContainer, RegistryAccess pRegistryAccess) {
        return ItemStack.EMPTY;
    }


    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        return ItemStack.EMPTY;
    }


    @Override
    public ResourceLocation getId() {
        return id;
    }





    @Override
    public RecipeSerializer<?> getSerializer() {
        return Register.RECIPE_SERIALIZER_QUANTUM_QUARRY.get();
    }

    @Override
    public RecipeType<?> getType() {
        return Register.QUANTUM_QUARRY.get();
    }

    public String getRecipeType() {
      return   recipeType;
    }

    public String getTypeOperation() {
        return recipeOperation;
    }

    public List<ItemStack> getInputs() {
        return inputs;
    }
}
