package com.denfop.integration.jei.alloysmelter;

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

public class AlloySmelterCategory extends Gui implements IRecipeCategory<AlloySmelterRecipeWrapper> {

    private final IDrawableStatic bg;
    private int progress = 0;
    private int energy = 0;

    public AlloySmelterCategory(
            final IGuiHelper guiHelper
    ) {
        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/guialloysmelter" +
                        ".png"), 5, 16, 140,
                65
        );
    }

    @Override
    public String getUid() {
        return BlockBaseMachine.alloy_smelter.getName();
    }

    @Override
    public String getTitle() {
        return Localization.translate(new ItemStack(IUItem.machines, 1, 4).getUnlocalizedName());
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
        double energylevel = Math.min(14.0F * energy / 100, 14);
        double xScale = 24.0F * progress / 100;
        if (xScale > 24.0F) {
            progress = 0;
        }

        mc.getTextureManager().bindTexture(getTexture());
        drawTexturedModalRect(51 + 1, 20 + 14 - (int) energylevel, 176, 14 - (int) energylevel,
                14, (int) energylevel
        );
        drawTexturedModalRect(74, 18, 176, 14, (int) (xScale + 1), 16);

    }

    @Override
    public void setRecipe(
            final IRecipeLayout layout,
            final AlloySmelterRecipeWrapper recipes,
            final IIngredients ingredients
    ) {
        IGuiItemStackGroup isg = layout.getItemStacks(); // Группа ItemStack, которая нужна для рендера.
        isg.init(0, true, 32, 0); // Инициализируем слот 0. true/false - это обозначение того, является ли слот *ВХОДНЫМ*, true
        // - да, false - нет. Остальные 2 числа - координаты. X/Y.
        isg.set(0, recipes.getInput()); // Добавляем в слот 0 входной предмет.
        isg.init(1, true, 68, 0); // Инициализируем слот 0. true/false - это обозначение того, является ли слот *ВХОДНЫМ*, true
        // - да, false - нет. Остальные 2 числа - координаты. X/Y.
        isg.set(1, recipes.getInput1()); // Добавляем в слот 0 входной предмет.

        isg.init(2, false, 110, 17); // Инициализируем слот 1.  true/false - это обозначение того, является ли слот *ВХОДНЫМ*,
        // true - да, false - нет. Остальные 2 числа - координаты. X/Y.
        isg.set(2, recipes.getOutput()); // Добавляем в слот 1 выходной предмет.
    }

    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guialloysmelter.png");
    }


}
