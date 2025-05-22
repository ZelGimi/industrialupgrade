package com.denfop.integration.jei.rubbertree;

import com.denfop.Constants;
import com.denfop.IUItem;
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

public class RubberTreeCategory extends Gui implements IRecipeCategory<RubberTreeWrapper> {

    private final IDrawableStatic bg;


    public RubberTreeCategory(
            final IGuiHelper guiHelper
    ) {
        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/common3.png"), 3, 3, 200,
                150
        );
    }

    @Nonnull
    @Override
    public String getUid() {
        return "jei.raw_latex";
    }

    @Nonnull
    @Override
    public String getTitle() {
        return new ItemStack(IUItem.rawLatex).getDisplayName();
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
            final RubberTreeWrapper recipes,
            @Nonnull final IIngredients ingredients
    ) {
        IGuiItemStackGroup isg = layout.getItemStacks();
        isg.init(0, true, 5, 55);
        isg.set(0, new ItemStack(IUItem.treetap));
        isg.init(1, false, 180, 55);
        isg.set(1, new ItemStack(IUItem.rawLatex));
    }

    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guivein.png");
    }


}
