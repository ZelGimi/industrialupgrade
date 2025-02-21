package com.denfop.integration.jei.fluidseparator;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.*;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.blocks.mechanism.BlockRefiner;
import com.denfop.gui.GuiIU;
import com.denfop.integration.jei.JEICompat;
import com.denfop.tiles.mechanism.TileOilRefiner;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IGuiFluidStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.List;

public class FluidSeparatorCategory extends GuiIU implements IRecipeCategory<FluidSeparatorRecipeWrapper> {

    private final IDrawableStatic bg;


    public FluidSeparatorCategory(
            final IGuiHelper guiHelper
    ) {
        super(((TileOilRefiner) BlockRefiner.refiner.getDummyTe()).getGuiContainer(Minecraft.getMinecraft().player));

        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine" +
                        ".png"), 3, 3, 140,
                77
        );
        this.componentList.clear();
        this.addElement(TankGauge.createNormal(this, 12, 20, ((TileOilRefiner) container.base).fluidTank1));
        this.addElement(TankGauge.createNormal(this, 60, 20, ((TileOilRefiner) container.base).fluidTank2));
        this.addElement(TankGauge.createNormal(this, 108, 20, ((TileOilRefiner) container.base).fluidTank2));

    }

    @Nonnull
    @Override
    public String getUid() {
        return BlockBaseMachine3.fluid_separator.getName();
    }

    @Nonnull
    @Override
    public String getTitle() {
        return Localization.translate((JEICompat.getBlockStack(BlockBaseMachine3.fluid_separator)).getUnlocalizedName());
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

        new GuiComponent(this, 35, 38, EnumTypeComponent.FLUID_PART1,
                new Component<>(new ComponentEmpty())
        ).drawBackground(this.guiLeft, this.guiTop);
        new GuiComponent(this, 88, 40, EnumTypeComponent.PLUS_BUTTON,
                new Component<>(new ComponentEmpty())
        ).drawBackground(this.guiLeft, this.guiTop);
        for (final GuiElement<?> element : ((List<GuiElement<?>>) this.elements)) {
            element.drawBackground(this.guiLeft, this.guiTop);
        }

    }

    @Override
    public void setRecipe(
            final IRecipeLayout layout,
            final FluidSeparatorRecipeWrapper recipes,
            @Nonnull final IIngredients ingredients
    ) {


        IGuiFluidStackGroup fff = layout.getFluidStacks();

        fff.init(0, true, 16, 24, 12, 47, 10000, true, null);
        fff.set(0, recipes.getInput());

        fff.init(1, false, 16 + 48, 24, 12, 47, 10000, true, null);
        fff.set(1, recipes.getOutputs().get(0));


        fff.init(2, false, 16 + 96, 24, 12, 47, 10000, true, null);
        fff.set(2, recipes.getOutputs().get(1));


    }

    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guioilrefiner.png");
    }


}
