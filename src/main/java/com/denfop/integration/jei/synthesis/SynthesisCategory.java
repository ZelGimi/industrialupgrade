package com.denfop.integration.jei.synthesis;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.blocks.mechanism.BlockBaseMachine1;
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

public class SynthesisCategory extends Gui implements IRecipeCategory<SynthesisWrapper> {

    private final IDrawableStatic bg;
    private int progress = 0;
    private int energy = 0;

    public SynthesisCategory(
            final IGuiHelper guiHelper
    ) {
        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/guisynthesis" +
                        ".png"), 3, 3, 148,
                80
        );
    }

    @Nonnull
    @Override
    public String getUid() {
        return BlockBaseMachine1.synthesis.getName();
    }

    @Nonnull
    @Override
    public String getTitle() {
        return Localization.translate(new ItemStack(IUItem.basemachine, 1, 11).getUnlocalizedName());
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
        int xScale = 15 * progress / 100;
        int xScale1 = 20 * progress / 100;
        if (xScale > 15) {
            progress = 0;
        }

        mc.getTextureManager().bindTexture(getTexture());

        drawTexturedModalRect(+129, 32 + 14 - energylevel, 176, 14 - energylevel,
                14, energylevel
        );


        drawTexturedModalRect(+39, +32, 177, 33, xScale + 1, 14);

        drawTexturedModalRect(+75, +30, 177, 52, xScale1 + 1, 23);

    }

    @Override
    public void setRecipe(
            final IRecipeLayout layout,
            final SynthesisWrapper recipes,
            @Nonnull final IIngredients ingredients
    ) {
        IGuiItemStackGroup isg = layout.getItemStacks();
        isg.init(0, true, 19, 30);
        isg.set(0, recipes.getInput());
        isg.init(1, true, 55, 30);
        isg.set(1, recipes.getInput1());
        isg.init(2, false, 107, 30);
        isg.set(2, recipes.getOutput());
    }

    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guisynthesis.png");
    }


}
