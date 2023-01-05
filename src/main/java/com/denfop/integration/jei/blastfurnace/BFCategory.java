package com.denfop.integration.jei.blastfurnace;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.blocks.mechanism.BlockBlastFurnace;
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

public class BFCategory extends Gui implements IRecipeCategory<BFWrapper> {

    private final IDrawableStatic bg;


    public BFCategory(
            final IGuiHelper guiHelper
    ) {
        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/guivein.png"), 3, 3, 200,
                150
        );
    }

    @Nonnull
    @Override
    public String getUid() {
        return BlockBlastFurnace.blast_furnace_main.getName();
    }

    @Nonnull
    @Override
    public String getTitle() {
        return Localization.translate("iu.blastfurnace.info");
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
            final BFWrapper recipes,
            @Nonnull final IIngredients ingredients
    ) {
        IGuiItemStackGroup isg = layout.getItemStacks();
        final ItemStack part = new ItemStack(IUItem.blastfurnace, 1, 5);
        final ItemStack main = new ItemStack(IUItem.blastfurnace, 1, 0);
        final ItemStack input = new ItemStack(IUItem.blastfurnace, 1, 1);
        final ItemStack heat = new ItemStack(IUItem.blastfurnace, 1, 2);
        final ItemStack output = new ItemStack(IUItem.blastfurnace, 1, 3);
        final ItemStack water = new ItemStack(IUItem.blastfurnace, 1, 4);

        for (int i = 0; i < 9; i++) {
            int x = i % 3;
            int y = i / 3;
            isg.init(i, true, 17 + x * 16, 100 + y * 14);
            isg.set(i, part);
        }
        for (int i = 0; i < 9; i++) {
            int x = i % 3;
            int y = i / 3;
            ItemStack k = part;
            if (i == 1) {
                k = water;
            }
            if (i == 3) {
                k = heat;
            }
            if (i == 5) {
                k = output;
            }
            if (i == 7) {
                k = main;
            }
            isg.init(i + 9, true, 17 + x * 16, 52 + y * 14);
            isg.set(i + 9, k);
        }
        for (int i = 0; i < 9; i++) {
            int x = i % 3;
            int y = i / 3;
            ItemStack k = part;
            if (i == 4) {
                k = input;
            }
            isg.init(i + 18, true, 17 + x * 16, 4 + y * 14);
            isg.set(i + 18, k);
        }
    }

    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guivein.png");
    }


}
