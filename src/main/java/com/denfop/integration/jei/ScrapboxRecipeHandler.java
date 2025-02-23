package com.denfop.integration.jei;

import com.denfop.Constants;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

public class ScrapboxRecipeHandler implements IRecipeHandler<ScrapboxRecipeWrapper> {

    public ScrapboxRecipeHandler() {
    }

    public Class<ScrapboxRecipeWrapper> getRecipeClass() {
        return ScrapboxRecipeWrapper.class;
    }

    public String getRecipeCategoryUid(ScrapboxRecipeWrapper recipe) {
        return Constants.MOD_ID + ".scrapbox";
    }

    public IRecipeWrapper getRecipeWrapper(ScrapboxRecipeWrapper recipe) {
        return recipe;
    }

    public boolean isRecipeValid(ScrapboxRecipeWrapper recipe) {
        return true;
    }

}
