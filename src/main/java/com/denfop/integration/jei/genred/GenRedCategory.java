package com.denfop.integration.jei.genred;

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

public class GenRedCategory extends Gui implements IRecipeCategory<GenRedWrapper> {

    private final IDrawableStatic bg;

    public GenRedCategory(
            final IGuiHelper guiHelper
    ) {
        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine" +
                        ".png"), 5, 5, 140,
                75
        );
    }

    @Nonnull
    @Override
    public String getUid() {
        return BlockBaseMachine3.redstone_generator.getName();
    }

    @Nonnull
    @Override
    public String getTitle() {
        return Localization.translate(JEICompat.getBlockStack(BlockBaseMachine3.redstone_generator).getUnlocalizedName());
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
            final GenRedWrapper recipes,
            @Nonnull final IIngredients ingredients
    ) {


        IGuiItemStackGroup isg = layout.getItemStacks();
        isg.init(0, true, 95, 30);
        isg.set(0, recipes.getInput2());
    }

    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }


}
