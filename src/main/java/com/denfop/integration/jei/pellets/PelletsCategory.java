package com.denfop.integration.jei.pellets;

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

public class PelletsCategory extends Gui implements IRecipeCategory<PelletsWrapper> {

    private final IDrawableStatic bg;

    public PelletsCategory(
            final IGuiHelper guiHelper
    ) {
        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/guipallet" +
                        ".png"), 5, 3, 165,
                77
        );

    }

    @Nonnull
    @Override
    public String getUid() {
        return BlockBaseMachine3.pallet_generator.getName();
    }

    @Nonnull
    @Override
    public String getTitle() {
        return Localization.translate(JEICompat.getBlockStack(BlockBaseMachine3.pallet_generator).getUnlocalizedName());
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
        for (int x = 30; x <= 86 - 18; x += 18) {
            for (int y = 18; y <= 36; y += 18) {
                this.drawTexturedModalRect(0 + x, 0 + y, 179, 30, 18, 18);
            }
        }

    }

    @Override
    public void setRecipe(
            final IRecipeLayout layout,
            final PelletsWrapper recipes,
            @Nonnull final IIngredients ingredients
    ) {


        IGuiItemStackGroup isg = layout.getItemStacks();
        for (int i = 0; i < recipes.getCol(); i++) {
            isg.init(i, true, 30 + (i % 3) * 18, 18 + (i / 3) * 18);
            isg.set(i, recipes.getInput2());
        }
      /*  fff.init(0, false, 95, 21, 12, 47, 10000, true, null);
        fff.set(0, new FluidStack(FluidName.fluiduu_matter.getInstance(), (int) Math.max(1,recipes.getEnergy())));
        IGuiItemStackGroup isg = layout.getItemStacks();
        isg.init(0, true, 30,10);
        isg.set(0, recipes.getInput2());*/
    }

    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guipallet.png");
    }


}
