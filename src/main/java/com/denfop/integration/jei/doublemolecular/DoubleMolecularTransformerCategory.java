package com.denfop.integration.jei.doublemolecular;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.blocks.mechanism.BlockDoubleMolecularTransfomer;
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

public class DoubleMolecularTransformerCategory extends Gui implements IRecipeCategory<DoubleMolecularTransformerRecipeWrapper> {

    private final IDrawableStatic bg;
    private int progress = 0;

    public DoubleMolecularTransformerCategory(
            final IGuiHelper guiHelper
    ) {
        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/guidoublemoleculartransformernew" +
                        ".png"), 8, 22, 155,
                70
        );
    }

    @Override
    public String getUid() {
        return BlockDoubleMolecularTransfomer.double_transformer.getName();
    }

    @Override
    public String getTitle() {
        return Localization.translate(new ItemStack(IUItem.blockdoublemolecular).getUnlocalizedName());
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
        double xScale = 15.0D * progress / 100;
        if (xScale > 15) {
            progress = 0;
        }
        mc.getTextureManager().bindTexture(getTexture());
        drawTexturedModalRect(15, 26, 221, 7, 10, (int) xScale);

    }

    @Override
    public void setRecipe(
            final IRecipeLayout layout,
            final DoubleMolecularTransformerRecipeWrapper recipes,
            final IIngredients ingredients
    ) {
        IGuiItemStackGroup isg = layout.getItemStacks(); // Группа ItemStack, которая нужна для рендера.
        isg.init(0, true, 2, 4); // Инициализируем слот 0. true/false - это обозначение того, является ли слот *ВХОДНЫМ*, true
        // - да, false - нет. Остальные 2 числа - координаты. X/Y.
        isg.set(0, recipes.getInput()); // Добавляем в слот 0 входной предмет.
        isg.init(1, true, 20, 4); // Инициализируем слот 0. true/false - это обозначение того, является ли слот *ВХОДНЫМ*, true
        // - да, false - нет. Остальные 2 числа - координаты. X/Y.
        isg.set(1, recipes.getInput1()); // Добавляем в слот 0 входной предмет.

        isg.init(2, false, 11, 45); // Инициализируем слот 1.  true/false - это обозначение того, является ли слот *ВХОДНЫМ*,
        // true - да, false - нет. Остальные 2 числа - координаты. X/Y.
        isg.set(2, recipes.getOutput()); // Добавляем в слот 1 выходной предмет.
    }

    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guidoublemoleculartransformernew.png");
    }


}
