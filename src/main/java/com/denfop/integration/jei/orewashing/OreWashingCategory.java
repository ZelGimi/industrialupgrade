package com.denfop.integration.jei.orewashing;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.blocks.mechanism.BlockMoreMachine3;
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

public class OreWashingCategory extends Gui implements IRecipeCategory<OreWashingWrapper> {

    private final IDrawableStatic bg;
    private int progress = 0;
    private int energy = 0;

    public OreWashingCategory(
            final IGuiHelper guiHelper
    ) {
        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/GUIMachine.png"), 3, 3, 140,
                80
        );
    }

    @Nonnull
    @Override
    public String getUid() {
        return BlockMoreMachine3.orewashing.getName();
    }

    @Nonnull
    @Override
    public String getTitle() {
        return Localization.translate(new ItemStack(IUItem.machines_base3, 1, 8).getUnlocalizedName());
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
        int energylevel = (int) Math.min(16.0F * energy / 100, 16);

        int xScale = 24 * progress / 100;
        if (xScale > 24) {
            progress = 0;
        }

        mc.getTextureManager().bindTexture(new ResourceLocation(Constants.MOD_ID, "textures/gui/gui_progressbars.png"));
        drawTexturedModalRect(
                +2, 45, 136, 4, 12,
                14
        );
        mc.getTextureManager().bindTexture(getTexture());
        drawTexturedModalRect(66 - 1, 12 + 19, 192 + 34 - 16, 14 + 24 * 3, 17, 24);
        drawTexturedModalRect(66 - 1, 12 + 19, 192 + 36, 14 + 24 * 3, 17, xScale + 1);
        drawTexturedModalRect(
                +4, 42 + 16 - energylevel, 176, 16 - energylevel, 16,
                energylevel
        );
        drawTexturedModalRect(66 - 1, 12 - 1, 238, 0, 18, 18);
        for (int i = 0; i < 3; i++) {
            drawTexturedModalRect(66 - 1 + 18 * i, 56 - 1, 238, 0, 18, 18);
        }
        drawTexturedModalRect(2, 44 + 16, 238, 0, 18, 18);


    }

    @Override
    public void setRecipe(
            final IRecipeLayout layout,
            final OreWashingWrapper recipes,
            @Nonnull final IIngredients ingredients
    ) {
        IGuiItemStackGroup isg = layout.getItemStacks();
        isg.init(0, true, 65, 11);
        isg.set(0, recipes.getInput());
        for (int i = 0; i < recipes.getOutputs().size(); i++) {
            isg.init(i + 1, false, 65 + 18 * i, 55);
            isg.set(i + 1, recipes.getOutput().get(i));
        }
    }

    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/GUIMachine.png");
    }


}
