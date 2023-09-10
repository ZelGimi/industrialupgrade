package com.denfop.integration.jei.fishmachine;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.blocks.mechanism.BlockBaseMachine2;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class FishMCategory extends Gui implements IRecipeCategory<FishMWrapper> {

    private final IDrawableStatic bg;
    private int energy = 0;
    private int progress = 0;

    public FishMCategory(
            final IGuiHelper guiHelper
    ) {
        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/GuiFisher.png"), 3, 3, 160,
                80
        );
    }

    @Nonnull
    @Override
    public String getUid() {
        return BlockBaseMachine2.fisher.getName();
    }

    @Nonnull
    @Override
    public String getTitle() {
        return Localization.translate(new ItemStack(IUItem.basemachine1, 1, 1).getUnlocalizedName());
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
        progress++;
        energy++;
        int energylevel = (int) Math.min(48.0F * energy / 100, 48);

        double xScale = 15.0F * progress / 100;
        if (xScale > 15.0F) {
            progress = 0;
        }
        mc.getTextureManager().bindTexture(getTexture());
        drawTexturedModalRect(
                140 + 1 + 2, 25 + 48 - energylevel, 176,
                48 - energylevel, 48, energylevel
        );
        drawTexturedModalRect(39, 43, 177, 48, (int) (xScale + 1), 14);

    }

    @Override
    public void setRecipe(
            final IRecipeLayout layout,
            final FishMWrapper recipes,
            @Nonnull final IIngredients ingredients
    ) {
        IGuiItemStackGroup isg = layout.getItemStacks();
        isg.init(0, true, 13, 41);
        isg.set(0, new ItemStack(Items.FISHING_ROD));
        isg.init(1, false, 61, 23);
        isg.set(1, recipes.getOutput());

    }

    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/GuiFisher.png");
    }


}
