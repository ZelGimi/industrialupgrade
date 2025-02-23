package com.denfop.integration.jei.cokeoven;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.blocks.FluidName;
import com.denfop.blocks.mechanism.BlockCokeOven;
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
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;

public class CokeOvenCategory extends Gui implements IRecipeCategory<CokeOvenWrapper> {

    private final IDrawableStatic bg;
    private int progress = 0;

    public CokeOvenCategory(
            final IGuiHelper guiHelper
    ) {
        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/guicokeoven" +
                        ".png"), 5, 5, 168,
                92
        );
    }

    @Nonnull
    @Override
    public String getUid() {
        return BlockCokeOven.coke_oven_main.getName() + "1";
    }

    @Nonnull
    @Override
    public String getTitle() {
        return Localization.translate(new ItemStack(IUItem.cokeoven, 1, 0).getUnlocalizedName());
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
        int xScale = (int) (38.0 * progress / 100D);

        if (xScale >= 38.0) {
            progress = 0;
        }

        mc.getTextureManager().bindTexture(getTexture());
        drawTexturedModalRect(+83, +41, 177, 19, xScale, 11);


    }

    @Override
    public void setRecipe(
            final IRecipeLayout layout,
            final CokeOvenWrapper recipes,
            @Nonnull final IIngredients ingredients
    ) {
        IGuiItemStackGroup isg = layout.getItemStacks();
        isg.init(0, true, 57, 38);

        isg.set(0, recipes.getInput());

        IGuiFluidStackGroup fff = layout.getFluidStacks();

        fff.init(1, true, 5, 17, 12, 47, 10000, true, null);
        fff.set(1, new FluidStack(FluidName.fluidsteam.getInstance(), 1000));

        fff.init(2, false, 128, 17, 12, 47, 10000, true, null);
        fff.set(2, recipes.getOutput());
    }

    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guicokeoven.png");
    }


}
