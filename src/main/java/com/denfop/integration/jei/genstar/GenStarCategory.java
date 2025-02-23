package com.denfop.integration.jei.genstar;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.blocks.mechanism.BlockBaseMachine1;
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

public class GenStarCategory extends Gui implements IRecipeCategory<GenStarRecipeManager> {

    private final IDrawableStatic bg;
    private int progress = 0;
    private int energy = 0;

    public GenStarCategory(
            final IGuiHelper guiHelper
    ) {
        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/GuiWitherMaker" +
                        ".png"), 3, 3, 147,
                78
        );
    }

    @Nonnull
    @Override
    public String getUid() {
        return BlockBaseMachine1.gen_wither.getName();
    }

    @Nonnull
    @Override
    public String getTitle() {
        return Localization.translate(new ItemStack(IUItem.basemachine, 1, 13).getUnlocalizedName());
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


        int xScale = (int) (22D * progress / 100D);
        if (xScale >= 22) {
            progress = 0;
        }


        mc.getTextureManager().bindTexture(getTexture());


        drawTexturedModalRect(+74, +32, 177, 0, xScale, 18);

    }

    @Override
    public void setRecipe(
            final IRecipeLayout layout,
            final GenStarRecipeManager recipes,
            @Nonnull final IIngredients ingredients
    ) {
        IGuiItemStackGroup isg = layout.getItemStacks();
        isg.init(0, true, 4, 23);

        isg.set(0, recipes.getInput());
        isg.init(1, true, 22, 23);
        isg.set(1, recipes.getInput1());
        isg.init(2, true, 40, 23);
        isg.set(2, recipes.getInput2());
        isg.init(3, true, 4, 41);
        isg.set(3, recipes.getInput3());
        isg.init(4, true, 22, 41);
        isg.set(4, recipes.getInput4());
        isg.init(5, true, 40, 41);
        isg.set(5, recipes.getInput5());
        isg.init(6, true, 22, 59);
        isg.set(6, recipes.getInput6());


        isg.init(7, false, 116, 32);
        isg.set(7, recipes.getOutput());
    }

    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/GuiWitherMaker.png");
    }


}
