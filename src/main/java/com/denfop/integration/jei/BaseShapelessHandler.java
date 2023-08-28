package com.denfop.integration.jei;

import com.denfop.api.crafting.BaseShapelessRecipe;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

import javax.annotation.Nonnull;

public class BaseShapelessHandler implements IRecipeHandler<BaseShapelessRecipe> {

    @Nonnull
    public Class<BaseShapelessRecipe> getRecipeClass() {
        return BaseShapelessRecipe.class;
    }

    @Nonnull
    public String getRecipeCategoryUid(BaseShapelessRecipe recipe) {
        return "minecraft.crafting";
    }

    @Nonnull
    public IRecipeWrapper getRecipeWrapper(@Nonnull BaseShapelessRecipe recipe) {
        return new BaseShapelessWrapper(recipe);
    }

    public boolean isRecipeValid(@Nonnull BaseShapelessRecipe recipe) {
        return true;
    }

}
