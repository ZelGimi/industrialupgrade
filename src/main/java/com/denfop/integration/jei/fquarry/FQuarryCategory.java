package com.denfop.integration.jei.fquarry;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.blocks.mechanism.BlockBaseMachine;
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

public class FQuarryCategory extends Gui implements IRecipeCategory<FQuarryWrapper> {

    private final IDrawableStatic bg;
    private int energy = 0;

    public FQuarryCategory(
            final IGuiHelper guiHelper
    ) {
        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/guiquantumquerry.png"), 3, 3, 160,
                80
        );
    }

    @Nonnull
    @Override
    public String getUid() {
        return BlockBaseMachine.quantum_quarry.getName() + "1";
    }

    @Nonnull
    @Override
    public String getTitle() {
        return Localization.translate(new ItemStack(IUItem.machines, 1, 8).getUnlocalizedName());
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


    }

    @Override
    public void setRecipe(
            final IRecipeLayout layout,
            final FQuarryWrapper recipes,
            @Nonnull final IIngredients ingredients
    ) {

        IGuiItemStackGroup isg = layout.getItemStacks();
        isg.init(0, true, 7, 20);
        isg.set(0, new ItemStack(IUItem.analyzermodule));
        isg.init(1, false, 31, 11);
        isg.set(1, recipes.getOutput());
        isg.init(2, true, 7, 38);
        isg.set(2, new ItemStack(IUItem.module9, 1));
    }

    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guiquantumquerry.png");
    }


}
