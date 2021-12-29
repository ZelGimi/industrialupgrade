package com.denfop.integration.jei.advrefiner;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.blocks.mechanism.BlockAdvRefiner;
import ic2.core.init.Localization;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IGuiFluidStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class AdvRefinerCategory extends Gui implements IRecipeCategory<AdvRefinerRecipeWrapper> {

    private final IDrawableStatic bg;
    private int energy = 0;
    public AdvRefinerCategory(
            final IGuiHelper guiHelper
    ) {
        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/guioilrefiner" +
                        ".png"), 5, 5, 140,
                75);
    }

    @Override
    public  String getUid() {
        return BlockAdvRefiner.adv_refiner.getName();
    }

    @Override
    public String getTitle() {
        return Localization.translate(new ItemStack(IUItem.oiladvrefiner).getUnlocalizedName());
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
        int energylevel = Math.min(29 * energy/100,29);
        mc.getTextureManager().bindTexture(getTexture());


        this.drawTexturedModalRect( + 34,  + 64, 177, 104, energylevel, 9);




    }

    @Override
    public void setRecipe(
            final IRecipeLayout layout,
            final AdvRefinerRecipeWrapper recipes,
            final IIngredients ingredients
    ) {


        IGuiFluidStackGroup fff = layout.getFluidStacks();

        fff.init(0, true, 11, 5, 12, 47, 10000, true, null);
        fff.set(0, recipes.getInput());

        fff.init(1, false, 73, 5, 12, 47, 10000, true, null);
        fff.set(1, recipes.getOutputs().get(0));


        fff.init(2, false, 105, 5, 12, 47, 10000, true, null);
        fff.set(2, recipes.getOutputs().get(1));


    }

    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guioilrefiner.png");
    }



}
