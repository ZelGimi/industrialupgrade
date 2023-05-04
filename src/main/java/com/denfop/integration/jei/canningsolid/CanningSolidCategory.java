package com.denfop.integration.jei.canningsolid;

import com.denfop.Constants;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.recipes.BasicRecipe;
import ic2.core.init.Localization;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class CanningSolidCategory extends Gui implements IRecipeCategory<CanningSolidRecipeWrapper> {

    private final IDrawableStatic bg;
    private int progress = 0;
    private int energy = 0;

    public CanningSolidCategory(
            final IGuiHelper guiHelper
    ) {
        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/guisolidcanner" +
                        ".png"), 5, 16, 140,
                65
        );
    }

    @Nonnull
    @Override
    public String getUid() {
        return BlockBaseMachine3.solid_canner_iu.getName();
    }

    @Nonnull
    @Override
    public String getTitle() {
        return Localization.translate(BasicRecipe.getBlockStack(BlockBaseMachine3.solid_canner_iu).getUnlocalizedName());
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
        progress++;
        energy++;
        double energylevel = Math.min(14.0F * energy / 100, 14);
        double xScale = 24.0F * progress / 100;
        if (xScale > 24.0F) {
            progress = 0;
        }

        mc.getTextureManager().bindTexture(getTexture());
        drawTexturedModalRect(11 - 9, 30 + 14 - (int) energylevel, 176, 14 - (int) energylevel,
                14, (int) energylevel
        );
        drawTexturedModalRect(88 - 5, 35 - 16, 176, 14, (int) (xScale + 1), 16);

    }

    @Override
    public void setRecipe(
            final IRecipeLayout layout,
            final CanningSolidRecipeWrapper recipes,
            @Nonnull final IIngredients ingredients
    ) {
        IGuiItemStackGroup isg = layout.getItemStacks();
        isg.init(0, true, 31, 19);
        isg.set(0, recipes.getInput());
        isg.init(1, true, 61, 19);
        isg.set(1, recipes.getInput1());
        isg.init(2, false, 110, 19);
        isg.set(2, recipes.getOutput());
    }

    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guisolidcanner.png");
    }


}
