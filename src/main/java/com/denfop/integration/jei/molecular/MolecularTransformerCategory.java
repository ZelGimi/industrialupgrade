package com.denfop.integration.jei.molecular;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.blocks.mechanism.BlockMolecular;
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

public class MolecularTransformerCategory extends Gui implements IRecipeCategory<MolecularTransformerRecipeWrapper> {

    private final IDrawableStatic bg;
    private int progress = 0;

    public MolecularTransformerCategory(
            final IGuiHelper guiHelper
    ) {
        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/guimoleculartransformernew" +
                        ".png"), 10, 49, 203,
                73
        );
    }

    @Nonnull
    @Override
    public String getUid() {
        return BlockMolecular.molecular.getName();
    }

    @Nonnull
    @Override
    public String getTitle() {
        return Localization.translate(new ItemStack(IUItem.blockmolecular).getUnlocalizedName());
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
        drawTexturedModalRect(23 - 10, 75 - 49, 242, 32, 14, (int) xScale);

    }

    @Override
    public void setRecipe(
            final IRecipeLayout layout,
            final MolecularTransformerRecipeWrapper recipes,
            @Nonnull final IIngredients ingredients
    ) {
        IGuiItemStackGroup isg = layout.getItemStacks();
        isg.init(0, true, 11, 7);
        isg.set(0, recipes.getInput());
        isg.init(1, false, 11, 47);
        isg.set(1, recipes.getOutput());
    }

    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimoleculartransformernew.png");
    }


}
