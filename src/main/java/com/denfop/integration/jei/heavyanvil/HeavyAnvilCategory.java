package com.denfop.integration.jei.heavyanvil;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.blocks.BlockStrongAnvil;
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

public class HeavyAnvilCategory extends Gui implements IRecipeCategory<HeavyAnvilWrapper> {

    private final IDrawableStatic bg;

    public HeavyAnvilCategory(
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
        return BlockStrongAnvil.block_strong_anvil.getName();
    }

    @Nonnull
    @Override
    public String getTitle() {
        return Localization.translate(JEICompat.getBlockStack(BlockStrongAnvil.block_strong_anvil).getUnlocalizedName());
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
            final HeavyAnvilWrapper recipes,
            @Nonnull final IIngredients ingredients
    ) {
        IGuiItemStackGroup isg = layout.getItemStacks();
        isg.init(0, true, 5, 25);
        isg.set(0, recipes.getInput());
        isg.init(1, true, 30, 25);
        isg.set(1, new ItemStack(IUItem.ObsidianForgeHammer));
        isg.init(2, false, 60, 25);
        isg.set(2, recipes.getOutput());


    }

    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guivein.png");
    }


}
