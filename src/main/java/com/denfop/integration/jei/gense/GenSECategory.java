package com.denfop.integration.jei.gense;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.blocks.mechanism.BlockSolarEnergy;
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

public class GenSECategory extends Gui implements IRecipeCategory<GenSEWrapper> {

    private final IDrawableStatic bg;
    private int progress = 0;
    private int energy = 0;

    public GenSECategory(
            final IGuiHelper guiHelper
    ) {
        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/sunnariumgenerator.png"), 3, 3, 160,
                75);
    }

    @Override
    public  String getUid() {
        return BlockSolarEnergy.se_gen.getName();
    }

    @Override
    public String getTitle() {
        return Localization.translate(new ItemStack(IUItem.blockSE,1,0).getUnlocalizedName());
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
        mc.getTextureManager().bindTexture(getTexture());
         drawTexturedModalRect( 120,  31, 176, 14, 24 + 1, 16);

    }

    @Override
    public void setRecipe(
            final IRecipeLayout layout,
            final GenSEWrapper recipes,
            final IIngredients ingredients
    ) {
        IGuiItemStackGroup isg = layout.getItemStacks(); // Группа ItemStack, которая нужна для рендера.

        isg.init(0, false, 65, 31); // Инициализируем слот 1.  true/false - это обозначение того, является ли слот *ВХОДНЫМ*,
        isg.set(0, recipes.getOutput()); // Добавляем в слот 1 выходной предмет.
    }

    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/sunnariumgenerator.png");
    }



}
