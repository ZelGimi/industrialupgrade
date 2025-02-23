package com.denfop.integration.jei.gas_well;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.blocks.mechanism.BlockGasWell;
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
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class GasWellCategory extends Gui implements IRecipeCategory<GasWellWrapper> {

    private final IDrawableStatic bg;

    public GasWellCategory(
            final IGuiHelper guiHelper
    ) {

        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/common3" +
                        ".png"), 3, 3, 140,
                140
        );
    }

    @Nonnull
    @Override
    public String getUid() {
        return BlockGasWell.gas_well_controller.getName();
    }

    @Nonnull
    @Override
    public String getTitle() {
        return Localization.translate(JEICompat.getBlockStack(BlockGasWell.gas_well_controller).getUnlocalizedName());
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


    }

    @Override
    public void setRecipe(
            final IRecipeLayout layout,
            final GasWellWrapper recipes,
            @Nonnull final IIngredients ingredients
    ) {



    }

    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guivein.png");
    }


}
