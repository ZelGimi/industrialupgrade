package com.denfop.integration.jei.quarry_mac;

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

public class MQuarryCategory extends Gui implements IRecipeCategory<MQuarryWrapper> {

    private final IDrawableStatic bg;
    private int energy = 0;

    public MQuarryCategory(
            final IGuiHelper guiHelper
    ) {
        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/guiquantumquerry.png"), 3, 3, 160,
                80
        );
    }

    @Nonnull
    @Override
    public String getUid() {
        return BlockBaseMachine.quantum_quarry.getName() + "3";
    }

    @Nonnull
    @Override
    public String getTitle() {
        return Localization.translate(new ItemStack(IUItem.machines, 1, 8).getUnlocalizedName());
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
    public void drawExtras(final Minecraft mc) {

        energy++;
        int energylevel = (int) Math.min(48.0F * energy / 100, 48);


        mc.getTextureManager().bindTexture(getTexture());
        drawTexturedModalRect(
                140 + 1 + 2, 25 + 48 - energylevel, 176,
                48 - energylevel, 48, energylevel
        );

    }

    @Override
    public void setRecipe(
            final IRecipeLayout layout,
            final MQuarryWrapper recipes,
            @Nonnull final IIngredients ingredients
    ) {
        IGuiItemStackGroup isg = layout.getItemStacks();
        isg.init(0, true, 4, 11);
        isg.set(0, new ItemStack(IUItem.analyzermodule));
        isg.init(1, false, 26, 2);
        isg.set(1, recipes.getOutput());
        isg.init(2, true, 4, 29);
        isg.set(2, new ItemStack(IUItem.module9, 1, 14));
    }

    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guiquantumquerry.png");
    }


}
