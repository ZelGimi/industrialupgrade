package com.denfop.integration.jei.refiner;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.ComponentEmpty;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.gui.GuiElement;
import com.denfop.api.gui.TankGauge;
import com.denfop.blocks.mechanism.BlockRefiner;
import com.denfop.gui.GuiIU;
import com.denfop.tiles.mechanism.TileOilRefiner;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IGuiFluidStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.List;

public class RefinerCategory extends GuiIU implements IRecipeCategory<RefinerRecipeWrapper> {

    private final IDrawableStatic bg;


    public RefinerCategory(
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
        return BlockRefiner.refiner.getName();
    }

    @Nonnull
    @Override
    public String getTitle() {
        return Localization.translate(new ItemStack(IUItem.oilrefiner, 1).getUnlocalizedName());
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
        for (final GuiElement element : ((List<GuiElement>) this.elements)) {
            element.drawBackground(this.guiLeft, this.guiTop);
        }

    }

    @Override
    public void setRecipe(
            final IRecipeLayout layout,
            final RefinerRecipeWrapper recipes,
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
