package com.denfop.integration.jei.convertermatter;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.blocks.mechanism.BlockConverterMatter;
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

public class ConverterCategory extends Gui implements IRecipeCategory<ConverterWrapper> {

    private final IDrawableStatic bg;
    private int progress = 0;
    private int energy = 0;

    public ConverterCategory(
            final IGuiHelper guiHelper
    ) {
        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/guiconvertersolidmatter.png"), 3, 5,
                168,
                135
        );
    }

    @Nonnull
    @Override
    public String getUid() {
        return BlockConverterMatter.converter_matter.getName();
    }

    @Nonnull
    @Override
    public String getTitle() {
        return Localization.translate(new ItemStack(IUItem.convertersolidmatter, 1, 0).getUnlocalizedName());
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
        int energylevel = (int) Math.min(38.0F * energy / 100, 38);

        int xScale = 31 * progress / 100;
        if (xScale > 31) {
            progress = 0;
        }

        mc.getTextureManager().bindTexture(getTexture());
        drawTexturedModalRect(116, 110, 176,
                81, energylevel, 11
        );


        drawTexturedModalRect(76, 46, 176, 24, xScale, 15);
        drawTexturedModalRect((168) - xScale, 46, 208 - xScale, 24, xScale, 15);

        drawTexturedModalRect(113, 11 + 1, 176, 42, 17, xScale);

        drawTexturedModalRect(113, 96 - xScale, 176, 74 - xScale, 17, xScale);

    }

    @Override
    public void setRecipe(
            final IRecipeLayout layout,
            final ConverterWrapper recipes,
            @Nonnull final IIngredients ingredients
    ) {
        IGuiItemStackGroup isg = layout.getItemStacks();
        isg.init(0, false, 113, 45);
        isg.set(0, recipes.getOutput());
        isg.init(1, true, 47, 45);
        isg.set(1, recipes.getOutput());

    }

    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guiconvertersolidmatter.png");
    }


}
