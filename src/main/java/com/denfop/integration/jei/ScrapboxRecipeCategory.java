package com.denfop.integration.jei;


import com.denfop.Constants;
import com.denfop.Localization;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ScrapboxRecipeCategory extends BlankRecipeCategory<IRecipeWrapper> {

    public static final String UID = Constants.MOD_ID + ".scrapbox";
    private final IDrawable background;

    public ScrapboxRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(
                new ResourceLocation(Constants.MOD_ID + ":textures/gui/ScrapboxRecipes.png"),
                55,
                30,
                82,
                26
        );
    }

    public String getUid() {
        return Constants.MOD_ID + ".scrapbox";
    }

    public String getTitle() {
        return Localization.translate(Constants.ABBREVIATION + ".crafting.scrap_box");
    }

    public IDrawable getBackground() {
        return this.background;
    }

    public void setRecipe(IRecipeLayout recipeLayout, IRecipeWrapper recipeWrapper, IIngredients ingredients) {
        IGuiItemStackGroup itemStacks = recipeLayout.getItemStacks();
        itemStacks.init(0, true, 0, 4);
        itemStacks.init(1, true, 60, 4);
        itemStacks.set(0, ingredients.getInputs(ItemStack.class).get(0));
        itemStacks.set(1, ingredients.getOutputs(ItemStack.class).get(0));
    }

    public String getModName() {
        return Constants.MOD_ID;
    }

}
