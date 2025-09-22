package com.denfop.integration.jei.upgradeblock;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.blocks.mechanism.BlockUpgradeBlock;
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

public class UpgradeBlockCategory extends Gui implements IRecipeCategory<UpgradeBlockWrapper> {

    private final IDrawableStatic bg;
    private int progress = 0;
    private int energy = 0;

    public UpgradeBlockCategory(
            final IGuiHelper guiHelper
    ) {
        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/GuiUpgradeBlock" +
                        ".png"), 3, 3, 148,
                80
        );
    }

    @Nonnull
    @Override
    public String getUid() {
        return BlockUpgradeBlock.upgrade_block.getName();
    }

    @Nonnull
    @Override
    public String getTitle() {
        return Localization.translate(new ItemStack(IUItem.upgradeblock).getUnlocalizedName());
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

        int xScale = 27 * progress / 100;
        int xScale1 = 27 * progress / 100;
        if (xScale > 27) {
            progress = 0;
        }

        mc.getTextureManager().bindTexture(getTexture());

        drawTexturedModalRect(+33, +35, 180, 7, xScale + 1, 16);


        drawTexturedModalRect(+78, +35, 225, 7, xScale1 + 1, 16);


    }

    @Override
    public void setRecipe(
            final IRecipeLayout layout,
            final UpgradeBlockWrapper recipes,
            @Nonnull final IIngredients ingredients
    ) {
        IGuiItemStackGroup isg = layout.getItemStacks();
        isg.init(0, true, 15, 34);
        isg.set(0, recipes.getInput());
        isg.init(1, true, 60, 34);
        isg.set(1, recipes.getInput1());
        isg.init(2, false, 109, 34);
        isg.set(2, recipes.getOutput());
    }

    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/GuiUpgradeBlock.png");
    }


}
