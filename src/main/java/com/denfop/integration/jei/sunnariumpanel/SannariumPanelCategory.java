package com.denfop.integration.jei.sunnariumpanel;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.blocks.mechanism.BlockSunnariumPanelMaker;
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

public class SannariumPanelCategory extends Gui implements IRecipeCategory<SannariumPanelWrapper> {

    private final IDrawableStatic bg;
    private int progress = 0;
    private int energy = 0;

    public SannariumPanelCategory(
            final IGuiHelper guiHelper
    ) {
        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/guisunnariumpanelmaker" +
                        ".png"), 3, 3, 140,
                75
        );
    }

    @Override
    public String getUid() {
        return BlockSunnariumPanelMaker.gen_sunnarium.getName();
    }

    @Override
    public String getTitle() {
        return Localization.translate(new ItemStack(IUItem.sunnariummaker, 1).getUnlocalizedName());
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
        energy++;
        int energylevel = (int) Math.min(14.0F * energy / 100, 14);
        int xScale = 14 * progress / 100;

        if (xScale > 14) {
            progress = 0;
        }

        mc.getTextureManager().bindTexture(getTexture());


        drawTexturedModalRect(+22, +54 + 14 - energylevel, 176, 14 - energylevel,
                14, energylevel
        );


        drawTexturedModalRect(+71, +31, 177, 32, xScale + 1, 15);

    }

    @Override
    public void setRecipe(
            final IRecipeLayout layout,
            final SannariumPanelWrapper recipes,
            final IIngredients ingredients
    ) {
        IGuiItemStackGroup isg = layout.getItemStacks(); // Группа ItemStack, которая нужна для рендера.
        isg.init(0, true, 10, 30); // Инициализируем слот 0. true/false - это обозначение того, является ли слот *ВХОДНЫМ*, true
        // - да, false - нет. Остальные 2 числа - координаты. X/Y.
        isg.set(0, recipes.getInput()); // Добавляем в слот 0 входной предмет.
        isg.init(1, true, 32, 30); // Инициализируем слот 0. true/false - это обозначение того, является ли слот *ВХОДНЫМ*, true
        // - да, false - нет. Остальные 2 числа - координаты. X/Y.
        isg.set(1, recipes.getInput1()); // Добавляем в слот 0 входной предмет.

        isg.init(2, false, 102, 30); // Инициализируем слот 1.  true/false - это обозначение того, является ли слот *ВХОДНЫМ*,
        // true - да, false - нет. Остальные 2 числа - координаты. X/Y.
        isg.set(2, recipes.getOutput()); // Добавляем в слот 1 выходной предмет.
    }

    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guisunnariumpanelmaker.png");
    }


}
