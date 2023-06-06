package com.denfop.integration.jei.scrap;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.blocks.mechanism.BlockMoreMachine3;
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
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class ScrapCategory extends Gui implements IRecipeCategory<ScrapRecipeWrapper> {

    private final IDrawableStatic bg;
    private int progress = 0;
    private int energy = 0;

    public ScrapCategory(
            final IGuiHelper guiHelper
    ) {
        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine3.png"), 3, 3, 140,
                80
        );
    }

    @Nonnull
    @Override
    public String getUid() {
        return BlockMoreMachine3.assamplerscrap.getName();
    }

    @Nonnull
    @Override
    public String getTitle() {
        return Localization.translate(new ItemStack(IUItem.machines_base3, 1, 4).getUnlocalizedName());
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

        int xScale = 24 * progress / 100;
        if (xScale > 24) {
            progress = 0;
        }
        mc.getTextureManager().bindTexture(new ResourceLocation(Constants.MOD_ID, "textures/gui/gui_progressbars.png"));
        drawTexturedModalRect(
                +2, 45, 136, 4, 12,
                14
        );
        mc.getTextureManager().bindTexture(getTexture());
        drawTexturedModalRect(66, 12 + 19, 192 - 16, 14 + 24, 16, 24);

        drawTexturedModalRect(66 - 1, 12 + 19, 192, 14 + 24, 16, xScale + 1);

        drawTexturedModalRect(
                +2, 44 + 14 - energylevel, 176, 14 - energylevel, 14,
                energylevel
        );
        drawTexturedModalRect(66 - 1, 12 - 1, 238, 0, 18, 18);
        drawTexturedModalRect(66 - 1, 56 - 1, 238, 0, 18, 18);
        drawTexturedModalRect(2, 44 + 16, 238, 0, 18, 18);

    }

    @Override
    public void setRecipe(
            final IRecipeLayout layout,
            final ScrapRecipeWrapper recipes,
            @Nonnull final IIngredients ingredients
    ) {
        IGuiItemStackGroup isg = layout.getItemStacks();
        isg.init(0, true, 65, 11);
        isg.set(0, recipes.getInput());
        isg.init(1, false, 65, 55);
        isg.set(1, recipes.getOutput());
    }

    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine3.png");
    }


}
