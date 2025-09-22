package com.denfop.integration.jei.genrad1;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.integration.jei.JEICompat;
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

public class GenRad1Category extends Gui implements IRecipeCategory<GenRad1Wrapper> {

    private final IDrawableStatic bg;

    public GenRad1Category(
            final IGuiHelper guiHelper
    ) {
        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/guipurifier" +
                        ".png"), 5, 4, 165,
                77
        );

    }

    @Nonnull
    @Override
    public String getUid() {
        return BlockBaseMachine3.radiation_purifier.getName();
    }

    @Nonnull
    @Override
    public String getTitle() {
        return Localization.translate(JEICompat.getBlockStack(BlockBaseMachine3.radiation_purifier).getUnlocalizedName());
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
    public void drawExtras(final Minecraft mc) {


        mc.getTextureManager().bindTexture(getTexture());


    }

    @Override
    public void setRecipe(
            final IRecipeLayout layout,
            final GenRad1Wrapper recipes,
            @Nonnull final IIngredients ingredients
    ) {


        IGuiItemStackGroup isg = layout.getItemStacks();
        isg.init(0, false, 78 - 5, 35 - 3);
        isg.set(0, recipes.getInput2());
      /*  fff.init(0, false, 95, 21, 12, 47, 10000, true, null);
        fff.set(0, new FluidStack(FluidName.fluiduu_matter.getInstance(), (int) Math.max(1,recipes.getEnergy())));
        IGuiItemStackGroup isg = layout.getItemStacks();
        isg.init(0, true, 30,10);
        isg.set(0, recipes.getInput2());*/
    }

    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guipurifier.png");
    }


}
