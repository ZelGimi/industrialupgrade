package com.denfop.integration.jei.spacebody;

import com.denfop.Constants;
import com.denfop.Localization;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IGuiFluidStackGroup;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class SpaceBodyCategory extends Gui implements IRecipeCategory<SpaceBodyRecipeWrapper> {

    private final IDrawableStatic bg;

    public SpaceBodyCategory(
            final IGuiHelper guiHelper
    ) {

        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/common3" +
                        ".png"), 3, 3, 140,
                170
        );
    }

    @Nonnull
    @Override
    public String getUid() {
        return "spacebody_iu";
    }

    @Nonnull
    @Override
    public String getTitle() {
        return Localization.translate("spacebody_jei");
    }


    @Nonnull
    @Override
    public String getModName() {
        return Constants.MOD_NAME;
    }

    @Nonnull
    @Override
    public IDrawable getBackground() {
        return bg;
    }


    @Override
    public void drawExtras(@Nonnull final Minecraft mc) {


    }

    @Override
    public void setRecipe(
            final IRecipeLayout layout,
            final SpaceBodyRecipeWrapper recipes,
            @Nonnull final IIngredients ingredients
    ) {
        IGuiItemStackGroup isg = layout.getItemStacks();
        final IGuiFluidStackGroup fff = layout.getFluidStacks();
        int amount = recipes.getInputs1().size() + recipes.getOutputs().size();
        for (int i = 0; i < amount; i++) {
            int x = 5 + (i % 6) * 20;
            int y = 65 + (i / 6) * 19;
            if (i < recipes.getInputs1().size()) {
                isg.init(i, true, x, y);
                isg.set(i, recipes.getInputs1().get(i));
            } else {
                fff.init(
                        i - recipes.getInputs1().size(),
                        true,
                        x,
                        y,
                        16,
                        16,
                        recipes.getOutputs().get(i - recipes.getInputs1().size()).amount,
                        true,
                        null
                );
                fff.set(i - recipes.getInputs1().size(), recipes.getOutputs().get(i - recipes.getInputs1().size()));
            }

        }


    }

    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guivein.png");
    }


}
