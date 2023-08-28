package com.denfop.integration.jei;

import com.denfop.api.crafting.BaseRecipe;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

import javax.annotation.Nonnull;

public class BaseRecipeHandler implements IRecipeHandler<BaseRecipe> {

    @Nonnull
    public Class<BaseRecipe> getRecipeClass() {
        return BaseRecipe.class;
    }

    @Nonnull
    public String getRecipeCategoryUid(BaseRecipe recipe) {
        return "minecraft.crafting";
    }

    @Nonnull
    public IRecipeWrapper getRecipeWrapper(@Nonnull BaseRecipe recipe) {
        return new BaseRecipeWrapper(recipe);
    }

    public boolean isRecipeValid(@Nonnull BaseRecipe recipe) {
        return true;
    }

}
