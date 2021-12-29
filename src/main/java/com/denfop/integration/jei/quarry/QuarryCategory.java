package com.denfop.integration.jei.quarry;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.blocks.mechanism.BlockBaseMachine;
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

public class QuarryCategory extends Gui implements IRecipeCategory<QuarryWrapper> {

    private final IDrawableStatic bg;
    private final int progress = 0;
    private int energy = 0;

    public QuarryCategory(
            final IGuiHelper guiHelper
    ) {
        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/guiquantumquerry.png"), 3, 3, 160,
                80
        );
    }

    @Override
    public String getUid() {
        return BlockBaseMachine.quantum_quarry.getName();
    }

    @Override
    public String getTitle() {
        return Localization.translate(new ItemStack(IUItem.machines, 1, 8).getUnlocalizedName());
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

        energy++;
        int energylevel = (int) Math.min(48.0F * energy / 100, 48);


        mc.getTextureManager().bindTexture(getTexture());
        drawTexturedModalRect(+140 + 1 + 2, +25 + 48 - energylevel, 176,
                48 - energylevel, 48, energylevel
        );

    }

    @Override
    public void setRecipe(
            final IRecipeLayout layout,
            final QuarryWrapper recipes,
            final IIngredients ingredients
    ) {
        IGuiItemStackGroup isg = layout.getItemStacks(); // Группа ItemStack, которая нужна для рендера.
        isg.init(0, true, 4, 11); // Инициализируем слот 0. true/false - это обозначение того, является ли слот *ВХОДНЫМ*, true
        // - да, false - нет. Остальные 2 числа - координаты. X/Y.
        isg.set(0, new ItemStack(IUItem.analyzermodule)); // Добавляем в слот 0 входной предмет.

        isg.init(1, false, 26, 2); // Инициализируем слот 1.  true/false - это обозначение того, является ли слот *ВХОДНЫМ*,
        // true - да, false - нет. Остальные 2 числа - координаты. X/Y.
        isg.set(1, recipes.getOutput()); // Добавляем в слот 1 выходной предмет.
    }

    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guiquantumquerry.png");
    }


}
