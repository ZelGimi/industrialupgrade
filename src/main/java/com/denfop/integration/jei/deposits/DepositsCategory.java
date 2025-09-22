package com.denfop.integration.jei.deposits;

import com.denfop.Constants;
import com.denfop.Localization;
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
import java.util.List;

public class DepositsCategory extends Gui implements IRecipeCategory<DepositsWrapper> {

    private final IDrawableStatic bg;

    public DepositsCategory(
            final IGuiHelper guiHelper
    ) {

        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/common3.png"), 3, 3, 200,
                180
        );
    }

    @Nonnull
    @Override
    public String getUid() {
        return "deposists_iu";
    }

    @Nonnull
    @Override
    public String getTitle() {
        return Localization.translate("deposists.jei");
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
            final DepositsWrapper recipes,
            @Nonnull final IIngredients ingredients
    ) {
        IGuiItemStackGroup isg = layout.getItemStacks();

        final List<ItemStack> stackList = recipes.getInputs();
        for (int i = 0; i < stackList.size(); i++) {
            int x = 5 + (i / 8) * 40;
            int y = 15 + (i % 8) * 20;
            isg.init(i, true, x, y);
            isg.set(i, stackList.get(i));

        }


    }

    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guivein.png");
    }


}
