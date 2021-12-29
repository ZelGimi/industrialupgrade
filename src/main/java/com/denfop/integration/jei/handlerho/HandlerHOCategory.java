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

public class HandlerHOCategory extends Gui implements IRecipeCategory<HandlerHORecipeWrapper> {

    private final IDrawableStatic bg;
    private int progress = 0;
    private final int energy = 0;

    public HandlerHOCategory(
            final IGuiHelper guiHelper
    ) {
        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/GUIHandlerHO" +
                        ".png"), 3, 3, 140,
                75
        );
    }

    @Override
    public String getUid() {
        return BlockBaseMachine1.handler_ho.getName();
    }

    @Override
    public String getTitle() {
        return Localization.translate(new ItemStack(IUItem.basemachine, 1, 12).getUnlocalizedName());
    }

    @Override
    public String getModName() {
        return Constants.MOD_NAME;
    }

    @Override
    public IDrawable getBackground() {
        return bg;
    }


    @Override
    public void drawExtras(final Minecraft mc) {
        progress++;

        int xScale = 44 * progress / 100;
        if (xScale > 44) {
            progress = 0;
        }


        mc.getTextureManager().bindTexture(getTexture());
        drawTexturedModalRect(45, 28, 177, 32, xScale + 1, 14);


        drawTexturedModalRect(48, 49, 176, 50, 39, 11);


    }

    @Override
    public void setRecipe(
            final IRecipeLayout layout,
            final HandlerHORecipeWrapper recipes,
            final IIngredients ingredients
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
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/GUIHandlerHO.png");
    }


}
