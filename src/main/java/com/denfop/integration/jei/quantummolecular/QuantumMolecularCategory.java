package com.denfop.integration.jei.quantummolecular;

import com.denfop.Constants;
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

public class QuantumMolecularCategory extends Gui implements IRecipeCategory<QuantumMolecularRecipeWrapper> {

    private final IDrawableStatic bg;
    private int progress = 0;

    public QuantumMolecularCategory(
            final IGuiHelper guiHelper
    ) {
        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/guidoublemoleculartransformernew" +
                        ".png"), 10, 49, 216,
                73
        );
    }

    @Nonnull
    @Override
    public String getUid() {
        return BlockBaseMachine3.quantum_transformer.getName();
    }

    @Nonnull
    @Override
    public String getTitle() {
        return JEICompat.getBlockStack(BlockBaseMachine3.quantum_transformer).getDisplayName();
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
        double xScale = 18.0D * progress / 100;
        if (xScale > 18) {
            progress = 0;
        }
        mc.getTextureManager().bindTexture(getTexture());
        drawTexturedModalRect(31 - 11, 75 - 49, 43, 237, 14, (int) xScale);

    }

    @Override
    public void setRecipe(
            final IRecipeLayout layout,
            final QuantumMolecularRecipeWrapper recipes,
            @Nonnull final IIngredients ingredients
    ) {
        IGuiItemStackGroup isg = layout.getItemStacks();
        isg.init(0, true, 8, 7);
        isg.set(0, recipes.getInput());
        isg.init(1, true, 28, 7);
        isg.set(1, recipes.getInput1());
        isg.init(2, false, 18, 47);
        isg.set(2, recipes.getOutput());
    }

    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guidoublemoleculartransformernew.png");
    }


}
