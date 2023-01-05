package com.denfop.integration.jei.modularator;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.blocks.mechanism.BlockBaseMachine;
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

public class ModulatorCategory1 extends Gui implements IRecipeCategory<ModulatorWrapper1> {

    private final IDrawableStatic bg;

    public ModulatorCategory1(
            final IGuiHelper guiHelper
    ) {
        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/guimodulemachine_jei.png"), 3, 3, 169,
                78
        );
    }

    @Nonnull
    @Override
    public String getUid() {
        return BlockBaseMachine.modulator.getName() + "1";
    }

    @Nonnull
    @Override
    public String getTitle() {
        return Localization.translate(new ItemStack(IUItem.machines, 1, 9).getUnlocalizedName());
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
            final ModulatorWrapper1 recipes,
            @Nonnull final IIngredients ingredients
    ) {
        IGuiItemStackGroup isg = layout.getItemStacks();
        isg.init(
                0,
                true,
                81 - 4,
                14 - 4
        );
        isg.set(0, new ItemStack(IUItem.module9, 1, 12));
        isg.init(1, true, 5, 32);
        isg.set(1, recipes.getOutput());

    }

    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guiquantumquerry.png");
    }


}
