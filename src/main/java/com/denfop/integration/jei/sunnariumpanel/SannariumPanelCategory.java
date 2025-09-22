package com.denfop.integration.jei.sunnariumpanel;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.blocks.mechanism.BlockSunnariumPanelMaker;
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

public class SannariumPanelCategory extends Gui implements IRecipeCategory<SannariumPanelWrapper> {

    private final IDrawableStatic bg;
    private int progress = 0;
    private int energy = 0;

    public SannariumPanelCategory(
            final IGuiHelper guiHelper
    ) {
        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/guisunnariumpanelmaker" +
                        ".png"), 3, 3, 140,
                75
        );
    }

    @Nonnull
    @Override
    public String getUid() {
        return BlockSunnariumPanelMaker.gen_sunnarium.getName();
    }

    @Nonnull
    @Override
    public String getTitle() {
        return Localization.translate(new ItemStack(IUItem.sunnariummaker, 1).getUnlocalizedName());
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
        int energylevel = (int) Math.min(14.0F * energy / 100, 14);
        int xScale = 14 * progress / 100;

        if (xScale > 14) {
            progress = 0;
        }

        mc.getTextureManager().bindTexture(getTexture());


        drawTexturedModalRect(+22, 54 + 14 - energylevel, 176, 14 - energylevel,
                14, energylevel
        );


        drawTexturedModalRect(+74, +33, 177, 15, xScale + 1, 15);

    }

    @Override
    public void setRecipe(
            final IRecipeLayout layout,
            final SannariumPanelWrapper recipes,
            @Nonnull final IIngredients ingredients
    ) {
        IGuiItemStackGroup isg = layout.getItemStacks();
        isg.init(0, true, 25, 32);
        isg.set(0, recipes.getInput());
        isg.init(1, true, 47, 32);
        isg.set(1, recipes.getInput1());
        isg.init(2, false, 102, 32);
        isg.set(2, recipes.getOutput());
    }

    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guisunnariumpanelmaker.png");
    }


}
