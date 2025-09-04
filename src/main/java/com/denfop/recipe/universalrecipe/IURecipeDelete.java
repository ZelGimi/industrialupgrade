package com.denfop.recipe.universalrecipe;

import com.denfop.recipe.IInputItemStack;
import com.denfop.register.Register;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.List;

public class IURecipeDelete implements Recipe<Container> {
    private final ResourceLocation id;
    private final String recipeType;
    private final boolean isFluid;
    private final List<IInputItemStack> inputItemStackList;
    private final boolean removeAll;


    public IURecipeDelete(ResourceLocation id, String recipeType, Boolean isFluidRecipe, List<IInputItemStack> inputItemStackList, boolean removeAll) {
        this.id = id;
        this.recipeType = recipeType;
        this.inputItemStackList = inputItemStackList;
        this.removeAll = removeAll;
        this.isFluid = isFluidRecipe;
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

    public boolean isFluid() {
        return isFluid;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    public boolean isRemoveAll() {
        return removeAll;
    }

    public List<IInputItemStack> getInputsAll() {
        return inputItemStackList;
    }

    public String getRecipeType() {
        return recipeType;
    }


    @Override
    public RecipeSerializer<?> getSerializer() {
        return Register.RECIPE_SERIALIZER_IU_DELETE.get();
    }

    @Override
    public RecipeType<?> getType() {
        return Register.UNIVERSAL_RECIPE_TYPE_DELETE.get();
    }
}
