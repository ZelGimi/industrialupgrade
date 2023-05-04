package com.denfop.integration.jei.genstar;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.blocks.mechanism.BlockBaseMachine1;
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

public class GenStarCategory extends Gui implements IRecipeCategory<GenStarRecipeManager> {

    private final IDrawableStatic bg;
    private int progress = 0;
    private int energy = 0;

    public GenStarCategory(
            final IGuiHelper guiHelper
    ) {
        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/GuiWitherMaker" +
                        ".png"), 3, 3, 147,
                75
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
        energy++;
        int energylevel = (int) Math.min(14.0F * energy / 100, 14);
        int xScale = 40 * progress / 100;
        if (xScale > 40) {
            progress = 0;
        }

        mc.getTextureManager().bindTexture(getTexture());

        drawTexturedModalRect(+76, 51 + 11 - energylevel, 176, 14 - energylevel,
                14, energylevel
        );


        drawTexturedModalRect(+78, +13, 177, 19, xScale + 1, 18);

    }

    @Override
    public void setRecipe(
            final IRecipeLayout layout,
            final GenStarRecipeManager recipes,
            @Nonnull final IIngredients ingredients
    ) {
        IGuiItemStackGroup isg = layout.getItemStacks();
        isg.init(0, true, 7, 4);

        isg.set(0, recipes.getInput());
        isg.init(1, true, 25, 4);
        isg.set(1, recipes.getInput1());
        isg.init(2, true, 43, 4);
        isg.set(2, recipes.getInput2());
        isg.init(3, true, 7, 22);
        isg.set(3, recipes.getInput3());
        isg.init(4, true, 25, 22);
        isg.set(4, recipes.getInput4());
        isg.init(5, true, 43, 22);
        isg.set(5, recipes.getInput5());
        isg.init(6, true, 25, 40);
        isg.set(6, recipes.getInput6());


        isg.init(7, false, 127, 9);
        isg.set(7, recipes.getOutput());
    }

    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/GuiWitherMaker.png");
    }


}
