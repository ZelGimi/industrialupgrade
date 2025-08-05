package com.denfop.recipe.universalrecipe;

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

public class PlanetRecipe implements Recipe<Container> {
    private final ResourceLocation id;
    private final String recipeType;
    private final  List<ItemStack> inputs;
    private final String typeOperation;

    public PlanetRecipe(ResourceLocation id, String recipeType,  List<ItemStack> inputs, String typeOperation) {
        this.id = id;
        this.recipeType = recipeType;
        this.inputs = inputs;
        this.typeOperation = typeOperation;
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

    public  List<ItemStack> getInputs() {
        return inputs;
    }

    public String getTypeOperation() {
        return typeOperation;
    }



    public String getRecipeType() {
        return recipeType;
    }



    @Override
    public RecipeSerializer<?> getSerializer() {
        return Register.RECIPE_SERIALIZER_PLANET_RECIPE.get();
    }

    @Override
    public RecipeType<?> getType() {
        return Register.PLANET_RECIPE.get();
    }
}
