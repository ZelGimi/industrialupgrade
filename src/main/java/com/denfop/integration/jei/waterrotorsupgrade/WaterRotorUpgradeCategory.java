package com.denfop.integration.jei.waterrotorsupgrade;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
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

public class WaterRotorUpgradeCategory extends Gui implements IRecipeCategory<WaterRotorUpgradeWrapper> {

    private final IDrawableStatic bg;

    public WaterRotorUpgradeCategory(
            final IGuiHelper guiHelper
    ) {
        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/guirotorsupgrade_jei" +
                        ".png"), 5, 5, 140,
                80
        );
    }

    @Nonnull
    @Override
    public String getUid() {
        return BlockBaseMachine3.water_modifier.getName();
    }

    @Nonnull
    @Override
    public String getTitle() {
        return Localization.translate(new ItemStack(IUItem.basemachine2, 1, 44).getUnlocalizedName());
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
            final WaterRotorUpgradeWrapper recipes,
            @Nonnull final IIngredients ingredients
    ) {
        IGuiItemStackGroup isg = layout.getItemStacks();

        isg.init(0, true, 51 + 22, 36);
        isg.set(0, recipes.getInput()[1]);
        isg.init(1, true, 29 + 22, 36);
        isg.set(1, recipes.getInput()[0]);
    }

    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guirotorsupgrade_jei.png");
    }


}
