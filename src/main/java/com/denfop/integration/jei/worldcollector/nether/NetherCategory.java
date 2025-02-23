package com.denfop.integration.jei.worldcollector.nether;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
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

public class NetherCategory extends Gui implements IRecipeCategory<NetherWrapper> {

    private final IDrawableStatic bg;
    private int progress = 0;
    private int energy = 0;

    public NetherCategory(
            final IGuiHelper guiHelper
    ) {
        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/guinetherassembler" +
                        ".png"), 5, 5, 140,
                75
        );
    }

    @Nonnull
    @Override
    public String getUid() {
        return BlockBaseMachine3.nether_assembler.getName();
    }

    @Nonnull
    @Override
    public String getTitle() {
        return Localization.translate(new ItemStack(IUItem.basemachine2, 1, 37).getUnlocalizedName());
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
        int energylevel = (int) Math.min(51.0F * energy / 100, 51.0F);
        int xScale = (int) (34 * progress / 100);

        if (xScale > 34) {
            progress = 0;
        }

        mc.getTextureManager().bindTexture(getTexture());


        drawTexturedModalRect(
                25 + 1, 12 + 51 - energylevel, 179, 2 + 51 - energylevel,
                5, energylevel
        );


        drawTexturedModalRect(+66 - 5, +34 - 5, 177, 60, xScale, 18);


    }

    @Override
    public void setRecipe(
            final IRecipeLayout layout,
            final NetherWrapper recipes,
            @Nonnull final IIngredients ingredients
    ) {
        IGuiItemStackGroup isg = layout.getItemStacks();
        isg.init(0, true, 38, 18);

        isg.set(0, recipes.getInput());


        isg.init(2, false, 104, 28);
        isg.set(2, recipes.getOutput());
    }

    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guinetherassembler.png");
    }


}
