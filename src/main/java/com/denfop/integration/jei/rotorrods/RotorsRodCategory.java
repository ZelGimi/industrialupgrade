package com.denfop.integration.jei.rotorrods;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
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

public class RotorsRodCategory extends Gui implements IRecipeCategory<RotorsRodWrapper> {

    private final IDrawableStatic bg;
    private int progress = 0;

    public RotorsRodCategory(
            final IGuiHelper guiHelper
    ) {
        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/guirotorrods" +
                        ".png"), 5, 5, 140,
                65
        );
    }

    @Nonnull
    @Override
    public String getUid() {
        return BlockBaseMachine3.rods_manufacturer.getName();
    }

    @Nonnull
    @Override
    public String getTitle() {
        return Localization.translate(new ItemStack(IUItem.basemachine2, 1, 21).getUnlocalizedName());
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
        double xScale = 24.0F * progress / 100;
        if (xScale > 24.0F) {
            progress = 0;
        }

        mc.getTextureManager().bindTexture(getTexture());

        drawTexturedModalRect(74, 30, 176, 14, (int) (xScale + 1), 16);

    }

    @Override
    public void setRecipe(
            final IRecipeLayout layout,
            final RotorsRodWrapper recipes,
            @Nonnull final IIngredients ingredients
    ) {
        IGuiItemStackGroup isg = layout.getItemStacks();
        for (int i = 0; i < recipes.getInput().length; i++) {
            isg.init(i, true, 32 + 18 * (i / 3), 10 + 18 * (i % 3));
            isg.set(i, recipes.getInput()[i]);
        }
        isg.init(recipes.getInput().length, false, 110, 28);
        isg.set(recipes.getInput().length, recipes.getOutput());
    }

    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guirotorrods.png");
    }


}
