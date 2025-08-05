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

import java.util.List;

public class IURecipeDelete implements Recipe<CraftingInput> {
    private final String recipeType;

    private final boolean isFluid;
    private final List<IInputItemStack> inputItemStackList;
    private final boolean removeAll;


    public IURecipeDelete(String recipeType, Boolean isFluidRecipe, List<IInputItemStack> inputItemStackList, boolean removeAll) {
        this.recipeType = recipeType;
        this.inputItemStackList = inputItemStackList;
        this.removeAll = removeAll;
        this.isFluid = isFluidRecipe;
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


    public boolean isFluid() {
        return isFluid;
    }

    public boolean isRemoveAll() {
        return removeAll;
    }

    public List<IInputItemStack> getInputsAll() {
        return inputItemStackList;
    }
}
