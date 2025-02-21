package com.denfop.integration.jei.canning;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.integration.jei.JEICompat;
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

public class CanningCategory extends Gui implements IRecipeCategory<CanningRecipeWrapper> {

    private final IDrawableStatic bg;
    private int progress = 0;
    private int energy = 0;

    public CanningCategory(
            final IGuiHelper guiHelper
    ) {
        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/guicanner_jei" +
                        ".png"), 5, 16, 140,
                82
        );
    }

    @Nonnull
    @Override
    public String getUid() {
        return BlockBaseMachine3.canner_iu.getName();
    }

    @Nonnull
    @Override
    public String getTitle() {
        return Localization.translate(JEICompat.getBlockStack(BlockBaseMachine3.canner_iu).getUnlocalizedName());
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

        double xScale = 24.0F * progress / 100;
        if (xScale > 24.0F) {
            progress = 0;
        }

        mc.getTextureManager().bindTexture(getTexture());

        drawTexturedModalRect(69, 22 - 15, 178, 9, (int) (xScale), 16);

    }

    @Override
    public void setRecipe(
            final IRecipeLayout layout,
            final CanningRecipeWrapper recipes,
            @Nonnull final IIngredients ingredients
    ) {
        IGuiItemStackGroup isg = layout.getItemStacks();
        isg.init(0, true, 40 - 4, 1);
        isg.set(0, recipes.getInput());
        isg.init(1, true, 79 - 5, 43 - 15);
        isg.set(1, recipes.getInput1());
        isg.init(2, false, 118 - 5, 1);
        isg.set(2, recipes.getOutput());
        if (recipes.getFluidstack() != null) {
            IGuiFluidStackGroup fff = layout.getFluidStacks();
            fff.init(0, true, 43 - 4, 39 - 12, 12, 47, 10000, true, null);
            fff.set(0, recipes.getFluidstack());
        }
    }

    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guicanner_jei.png");
    }


}
