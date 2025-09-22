package com.denfop.integration.jei.microchip;

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

public class MicrochipCategory extends Gui implements IRecipeCategory<MicrochipRecipeWrapper> {

    private final IDrawableStatic bg;
    private int progress = 0;
    private int energy = 0;

    public MicrochipCategory(
            final IGuiHelper guiHelper
    ) {
        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/GUICirsuit" +
                        ".png"), 3, 3, 140,
                77
        );
    }

    @Nonnull
    @Override
    public String getUid() {
        return BlockBaseMachine.generator_microchip.getName();
    }

    @Nonnull
    @Override
    public String getTitle() {
        return Localization.translate(new ItemStack(IUItem.machines, 1, 6).getUnlocalizedName());
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
        double energylevel = Math.min(14.0F * energy / 100, 14);
        double xScale = 24D * progress / 100;
        double xScale1 = 10.0F * progress / 100;
        double xScale2 = 19.0F * progress / 100;
        if (xScale > 24.0F) {
            progress = 0;
        }

        mc.getTextureManager().bindTexture(getTexture());

        drawTexturedModalRect(25, 9, 176, 34, (int) (xScale + 1), 32);


        drawTexturedModalRect(57, 13, 176, 65, (int) (xScale1 + 1), 21);


        drawTexturedModalRect(86, 19, 176, 86, (int) (xScale2 + 1), 7);


        drawTexturedModalRect(2, 72 - 13 + 14 - (int) energylevel, 176, 14 - (int) energylevel,
                14, (int) energylevel
        );

    }

    @Override
    public void setRecipe(
            final IRecipeLayout layout,
            final MicrochipRecipeWrapper recipes,
            @Nonnull final IIngredients ingredients
    ) {
        IGuiItemStackGroup isg = layout.getItemStacks();
        isg.init(0, true, 6, 5);

        isg.set(0, recipes.getInput());
        isg.init(1, true, 6, 26);
        isg.set(1, recipes.getInput1());
        isg.init(2, true, 39, 5);
        isg.set(2, recipes.getInput2());
        isg.init(3, true, 39, 25);
        isg.set(3, recipes.getInput3());
        isg.init(4, true, 67, 15);
        isg.set(4, recipes.getInput4());
        isg.init(5, false, 108, 15);
        isg.set(5, recipes.getOutput());
    }

    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/GUICirsuit.png");
    }


}
