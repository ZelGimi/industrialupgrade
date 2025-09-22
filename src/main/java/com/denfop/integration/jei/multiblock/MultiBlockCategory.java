package com.denfop.integration.jei.multiblock;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.multiblock.MultiBlockStructure;
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

public class MultiBlockCategory extends Gui implements IRecipeCategory<MultiBlockWrapper> {

    private final IDrawableStatic bg;

    public MultiBlockCategory(
            final IGuiHelper guiHelper
    ) {

        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/common3" +
                        ".png"), 3, 3, 140,
                170
        );
    }

    @Nonnull
    @Override
    public String getUid() {
        return "multiblock_iu";
    }

    @Nonnull
    @Override
    public String getTitle() {
        return Localization.translate("multiblock.jei");
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
            final MultiBlockWrapper recipes,
            @Nonnull final IIngredients ingredients
    ) {
        IGuiItemStackGroup isg = layout.getItemStacks();

        final MultiBlockStructure structure = recipes.structure;
        for (int i = 0; i < structure.itemStackList.size(); i++) {
            int x = 5 + (i % 6) * 20;
            int y = 45 + (i / 6) * 19;
            isg.init(i, true, x, y);
            isg.set(i, structure.itemStackList.get(i));

        }
        double y = 2;
        isg.init(structure.itemStackList.size(), true, (int) (61),
                (int) (45 + 27 + y * 23)
        );
        isg.set(structure.itemStackList.size(), structure.itemStackList.get(0));

    }

    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guivein.png");
    }


}
