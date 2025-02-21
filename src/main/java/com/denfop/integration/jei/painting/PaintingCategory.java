package com.denfop.integration.jei.painting;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.blocks.mechanism.BlockBaseMachine2;
import com.denfop.utils.ModUtils;
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
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class PaintingCategory extends Gui implements IRecipeCategory<PaintingWrapper> {

    private final IDrawableStatic bg;
    private int progress = 0;
    private int energy = 0;

    public PaintingCategory(
            final IGuiHelper guiHelper
    ) {
        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/GUIPainter" +
                        ".png"), 3, 3, 140,
                75
        );
    }

    @Nonnull
    @Override
    public String getUid() {
        return BlockBaseMachine2.painter.getName();
    }

    @Nonnull
    @Override
    public String getTitle() {
        return Localization.translate(new ItemStack(IUItem.basemachine1, 1, 3).getUnlocalizedName());
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


        int xScale = (int) (9 * progress / 100D);
        if (xScale >= 9) {
            progress = 0;
        }

        mc.getTextureManager().bindTexture(getTexture());


        drawTexturedModalRect(76, 35, 179, 34, xScale, 13);


    }

    @Override
    public void setRecipe(
            final IRecipeLayout layout,
            final PaintingWrapper recipes,
            @Nonnull final IIngredients ingredients
    ) {
        IGuiItemStackGroup isg = layout.getItemStacks();
        isg.init(0, true, 25, 32);
        isg.set(0, recipes.getInput());
        isg.init(1, true, 47, 32);
        isg.set(1, recipes.getInput1());
        isg.init(2, false, 102, 32);
        final ItemStack item = recipes.getOutput();
        final NBTTagCompound nbt = ModUtils.nbt(item);
        nbt.setString("mode", recipes.nbt.getString("mode"));
        isg.set(2, item);
    }

    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/GUIPainter.png");
    }


}
