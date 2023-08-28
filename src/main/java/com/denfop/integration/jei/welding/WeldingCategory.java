package com.denfop.integration.jei.welding;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.blocks.TileBlockCreator;
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
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class WeldingCategory extends Gui implements IRecipeCategory<WeldingRecipeWrapper> {

    private final IDrawableStatic bg;
    private int progress = 0;
    private int energy = 0;

    public WeldingCategory(
            final IGuiHelper guiHelper
    ) {
        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/guialloysmelter" +
                        ".png"), 5, 16, 140,
                65
        );
    }

    @Nonnull
    @Override
    public String getUid() {
        return BlockBaseMachine3.welding.getName();
    }

    @Nonnull
    @Override
    public String getTitle() {
        return Localization.translate(TileBlockCreator.instance
                .get(BlockBaseMachine3.welding.getIdentifier())
                .getItemStack(BlockBaseMachine3.welding)
                .getUnlocalizedName());
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
        drawTexturedModalRect(51 + 1, 20 + 14 - (int) energylevel, 176, 14 - (int) energylevel,
                14, (int) energylevel
        );
        drawTexturedModalRect(74, 18, 176, 14, (int) (xScale + 1), 16);

    }

    @Override
    public void setRecipe(
            final IRecipeLayout layout,
            final WeldingRecipeWrapper recipes,
            @Nonnull final IIngredients ingredients
    ) {
        IGuiItemStackGroup isg = layout.getItemStacks();
        isg.init(0, true, 32, 0);
        isg.set(0, recipes.getInput());
        isg.init(1, true, 68, 0);
        isg.set(1, recipes.getInput1());
        isg.init(2, false, 110, 17);
        isg.set(2, recipes.getOutput());
    }

    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guialloysmelter.png");
    }


}
