package com.denfop.integration.jei.enrichment;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.blocks.mechanism.BlockBaseMachine1;
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

public class EnrichCategory extends Gui implements IRecipeCategory<EnrichRecipeWrapper> {

    private final IDrawableStatic bg;
    private int progress = 0;
    private int energy = 0;

    public EnrichCategory(
            final IGuiHelper guiHelper
    ) {
        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/guenrichment" +
                        ".png"), 3, 3, 140,
                75
        );
    }

    @Nonnull
    @Override
    public String getUid() {
        return BlockBaseMachine1.enrichment.getName();
    }


    @Nonnull
    @Override
    public String getTitle() {
        return Localization.translate(new ItemStack(IUItem.basemachine, 1, 10).getUnlocalizedName());
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
        double xScale = 15D * progress / 100;
        if (xScale > 15) {
            progress = 0;
        }

        mc.getTextureManager().bindTexture(getTexture());
        drawTexturedModalRect(+22, 54 + 14 - (int) energylevel, 176, 14 - (int) energylevel,
                14, (int) energylevel
        );


        drawTexturedModalRect(64 + 1, +32, 177, 32, (int) xScale + 1, 15);

    }

    @Override
    public void setRecipe(
            final IRecipeLayout layout,
            final EnrichRecipeWrapper recipes,
            @Nonnull final IIngredients ingredients
    ) {
        IGuiItemStackGroup isg = layout.getItemStacks();
        isg.init(0, true, 10, 30);
        isg.set(0, recipes.getInput());
        isg.init(1, true, 32, 30);
        isg.set(1, recipes.getInput1());

        isg.init(2, false, 102, 30);
        isg.set(2, recipes.getOutput());
    }

    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guenrichment.png");
    }


}
