package com.denfop.integration.jei.doublemolecular;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.blocks.mechanism.BlockDoubleMolecularTransfomer;
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

public class DoubleMolecularTransformerCategory extends Gui implements IRecipeCategory<DoubleMolecularTransformerRecipeWrapper> {

    private final IDrawableStatic bg;
    private int progress = 0;

    public DoubleMolecularTransformerCategory(
            final IGuiHelper guiHelper
    ) {
        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/guidoublemoleculartransformernew" +
                        ".png"), 8, 22, 155,
                70
        );
    }

    @Nonnull
    @Override
    public String getUid() {
        return BlockDoubleMolecularTransfomer.double_transformer.getName();
    }

    @Nonnull
    @Override
    public String getTitle() {
        return Localization.translate(new ItemStack(IUItem.blockdoublemolecular).getUnlocalizedName());
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
        double xScale = 15.0D * progress / 100;
        if (xScale > 15) {
            progress = 0;
        }
        mc.getTextureManager().bindTexture(getTexture());
        drawTexturedModalRect(15, 26, 221, 7, 10, (int) xScale);

    }

    @Override
    public void setRecipe(
            final IRecipeLayout layout,
            final DoubleMolecularTransformerRecipeWrapper recipes,
            @Nonnull final IIngredients ingredients
    ) {
        IGuiItemStackGroup isg = layout.getItemStacks();
        isg.init(0, true, 2, 4);
        isg.set(0, recipes.getInput());
        isg.init(1, true, 20, 4);
        isg.set(1, recipes.getInput1());
        isg.init(2, false, 11, 45);
        isg.set(2, recipes.getOutput());
    }

    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guidoublemoleculartransformernew.png");
    }


}
