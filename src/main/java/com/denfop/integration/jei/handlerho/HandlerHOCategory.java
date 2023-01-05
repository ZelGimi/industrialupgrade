package com.denfop.integration.jei.handlerho;

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

public class HandlerHOCategory extends Gui implements IRecipeCategory<HandlerHORecipeWrapper> {

    private final IDrawableStatic bg;
    private int energy = 0;
    private int progress = 0;

    public HandlerHOCategory(
            final IGuiHelper guiHelper
    ) {
        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/GUIHandlerHO_jei" +
                        ".png"), 3, 3, 140,
                80
        );
    }

    @Nonnull
    @Override
    public String getUid() {
        return BlockBaseMachine1.handler_ho.getName();
    }

    @Nonnull
    @Override
    public String getTitle() {
        return Localization.translate(new ItemStack(IUItem.basemachine, 1, 12).getUnlocalizedName());
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
        int xScale = 44 * progress / 100;
        if (xScale > 44) {
            progress = 0;
        }


        mc.getTextureManager().bindTexture(getTexture());
        drawTexturedModalRect(45, 28, 177, 32, xScale + 1, 14);
        drawTexturedModalRect(+22, 54 + 14 - energylevel, 176, 14 - energylevel,
                14, energylevel
        );

    }

    @Override
    public void setRecipe(
            final IRecipeLayout layout,
            final HandlerHORecipeWrapper recipes,
            @Nonnull final IIngredients ingredients
    ) {
        IGuiItemStackGroup isg = layout.getItemStacks();
        isg.init(0, true, 21, 25);
        isg.set(0, recipes.getInput());
        for (int i = 0; i < recipes.getOutput().size(); ++i) {
            isg.init(
                    1 + i,
                    false,
                    98,
                    7 + 18 * i
            );
            isg.set(1 + i, recipes.getOutput().get(i));

        }

    }

    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/GUIHandlerHO_jei.png");
    }


}
