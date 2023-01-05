package com.denfop.integration.jei.bf;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.Ic2Items;
import com.denfop.blocks.mechanism.BlockBlastFurnace;
import ic2.core.init.Localization;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IGuiFluidStackGroup;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;

public class BlastFCategory extends Gui implements IRecipeCategory<BlastFWrapper> {

    private final IDrawableStatic bg;
    private int progress = 0;

    public BlastFCategory(
            final IGuiHelper guiHelper
    ) {
        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/guiblastfurnace_jei" +
                        ".png"), 5, 5, 140,
                75
        );
    }

    @Nonnull
    @Override
    public String getUid() {
        return BlockBlastFurnace.blast_furnace_main.getName() + "1";
    }

    @Nonnull
    @Override
    public String getTitle() {
        return Localization.translate(new ItemStack(IUItem.blastfurnace, 1, 0).getUnlocalizedName());
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
        int xScale = 24 * progress / 100;

        if (xScale > 24) {
            progress = 0;
        }

        mc.getTextureManager().bindTexture(getTexture());
        drawTexturedModalRect(+74, +29, 176, 14, xScale + 1, 16);


    }

    @Override
    public void setRecipe(
            final IRecipeLayout layout,
            final BlastFWrapper recipes,
            @Nonnull final IIngredients ingredients
    ) {
        IGuiItemStackGroup isg = layout.getItemStacks();
        isg.init(0, true, 50, 34 - 6);

        isg.set(0, new ItemStack(Items.IRON_INGOT));

        IGuiFluidStackGroup fff = layout.getFluidStacks();

        fff.init(1, true, 5, 4, 12, 47, 10000, true, null);
        fff.set(1, new FluidStack(FluidRegistry.WATER, 1000));

        isg.init(2, false, 110, 28);
        isg.set(2, Ic2Items.advIronIngot);
    }

    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guiblastfurnace_jei.png");
    }


}
