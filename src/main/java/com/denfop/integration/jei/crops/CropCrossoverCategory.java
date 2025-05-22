package com.denfop.integration.jei.crops;

import com.denfop.Constants;
import com.denfop.Localization;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.List;

public class CropCrossoverCategory extends Gui implements IRecipeCategory<CropCrossoverWrapper> {

    private final IDrawableStatic bg;

    public CropCrossoverCategory(
            final IGuiHelper guiHelper
    ) {

        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/common3" +
                        ".png"), 3, 3, 140,
                140
        );
    }

    @Nonnull
    @Override
    public String getUid() {
        return "iu.crop";
    }

    @Nonnull
    @Override
    public String getTitle() {
        return Localization.translate("crop.jei");
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
            final CropCrossoverWrapper recipes,
            @Nonnull final IIngredients ingredients
    ) {
        final IGuiItemStackGroup isg = layout.getItemStacks();
        final List<ItemStack> list = recipes.getInputs();
        for (int i = 0; i < list.size(); i++) {
            int yDisplayPosition1 = 60 + (30 - list.size()) * i - list.size() * 16;
            isg.init(i, true, 60 - 21, yDisplayPosition1);
            isg.set(i, list.get(i));
        }
        isg.init(list.size(), false, 85, 44);
        isg.set(list.size(), recipes.getOutputs());

    }

    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guivein.png");
    }


}
