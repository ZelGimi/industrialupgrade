package com.denfop.integration.jei.sunnarium;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.blocks.mechanism.BlockSunnariumMaker;
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

public class SunnariumCategory extends Gui implements IRecipeCategory<SunnariumWrapper> {

    private final IDrawableStatic bg;
    private int progress = 0;
    private int energy = 0;

    public SunnariumCategory(
            final IGuiHelper guiHelper
    ) {
        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/GuiSunnariumMaker" +
                        ".png"), 3, 3, 140,
                77
        );
    }

    @Nonnull
    @Override
    public String getUid() {
        return BlockSunnariumMaker.gen_sunnarium_plate.getName();
    }

    @Nonnull
    @Override
    public String getTitle() {
        return Localization.translate(new ItemStack(IUItem.sunnariumpanelmaker).getUnlocalizedName());
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
        int xScale = (int) (17.0F * progress / 100);

        if (xScale > 17.0F) {
            progress = 0;
        }

        mc.getTextureManager().bindTexture(getTexture());


        drawTexturedModalRect(+9, 59 + 14 - energylevel, 176, 14 - energylevel,
                14, energylevel
        );


        drawTexturedModalRect(+52, +17, 176, 31, xScale + 1, 31);


    }

    @Override
    public void setRecipe(
            final IRecipeLayout layout,
            final SunnariumWrapper recipes,
            @Nonnull final IIngredients ingredients
    ) {
        IGuiItemStackGroup isg = layout.getItemStacks();
        isg.init(0, true, 34, 13);

        isg.set(0, recipes.getInput());
        isg.init(1, true, 70, 13);
        isg.set(1, recipes.getInput1());
        isg.init(2, true, 34, 35);
        isg.set(2, recipes.getInput2());
        isg.init(3, true, 70, 35);
        isg.set(3, recipes.getInput3());

        isg.init(4, false, 111, 30);
        isg.set(4, recipes.getOutput());
    }

    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/GuiSunnariumMaker.png");
    }


}
