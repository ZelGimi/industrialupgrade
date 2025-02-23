package com.denfop.integration.jei.genobs;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.blocks.mechanism.BlockBaseMachine2;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IGuiFluidStackGroup;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class GenObsCategory extends Gui implements IRecipeCategory<GenObsWrapper> {

    private final IDrawableStatic bg;
    private int energy = 0;
    private int progress = 0;

    public GenObsCategory(
            final IGuiHelper guiHelper
    ) {
        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/guiobsidiangenerator" +
                        ".png"), 5, 5, 145,
                78
        );
    }

    @Nonnull
    @Override
    public String getUid() {
        return BlockBaseMachine2.gen_obsidian.getName();
    }

    @Nonnull
    @Override
    public String getTitle() {
        return Localization.translate(new ItemStack(IUItem.basemachine1, 1, 10).getUnlocalizedName());
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

        int xScale = 32 * progress / 100;
        if (xScale > 32) {
            progress = 0;
        }

        mc.getTextureManager().bindTexture(getTexture());


        drawTexturedModalRect(83, 35, 177, 41, xScale, 19);


    }

    @Override
    public void setRecipe(
            final IRecipeLayout layout,
            final GenObsWrapper recipes,
            @Nonnull final IIngredients ingredients
    ) {


        IGuiFluidStackGroup fff = layout.getFluidStacks();
        IGuiItemStackGroup isg = layout.getItemStacks();
        fff.init(0, true, 42, 20, 12, 47, 10000, true, null);
        fff.set(0, recipes.getInput1());
        fff.init(1, true, 65, 20, 12, 47, 10000, true, null);
        fff.set(1, recipes.getInput2());
        isg.init(2, false, 121, 35);
        isg.set(2, recipes.getOutput());
    }

    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guiobsidiangenerator.png");
    }


}
