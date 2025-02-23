package com.denfop.integration.jei.aircollector;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.ComponentEmpty;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.gui.GuiElement;
import com.denfop.api.gui.TankGauge;
import com.denfop.blocks.mechanism.BlockAdvRefiner;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.gui.GuiIU;
import com.denfop.tiles.mechanism.TileAdvOilRefiner;
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

public class AirColCategory extends GuiIU implements IRecipeCategory<AirColRecipeWrapper> {

    private final IDrawableStatic bg;
    private int energy = 0;

    public AirColCategory(
            final IGuiHelper guiHelper
    ) {
        super(((TileAdvOilRefiner) BlockAdvRefiner.adv_refiner.getDummyTe()).getGuiContainer(Minecraft.getMinecraft().player));

        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine" +
                        ".png"), 3, 3, 140,
                77
        );
        this.componentList.clear();
        this.addElement(TankGauge.createNormal(this, 20, 20, ((TileAdvOilRefiner) container.base).getFluidTank(0)));
        this.addElement(TankGauge.createNormal(this, 60, 20, ((TileAdvOilRefiner) container.base).getFluidTank(1)));
        this.addElement(TankGauge.createNormal(this, 100, 20, ((TileAdvOilRefiner) container.base).getFluidTank(2)));

    }

    @Nonnull
    @Override
    public String getUid() {
        return BlockBaseMachine3.aircollector.getName();
    }

    @Nonnull
    @Override
    public String getTitle() {
        return Localization.translate(new ItemStack(IUItem.basemachine2, 1, 11).getUnlocalizedName());
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

        new GuiComponent(this, 44, 40, EnumTypeComponent.PLUS_BUTTON,
                new Component<>(new ComponentEmpty())
        ).drawBackground(this.guiLeft, this.guiTop);
        new GuiComponent(this, 84, 40, EnumTypeComponent.PLUS_BUTTON,
                new Component<>(new ComponentEmpty())
        ).drawBackground(this.guiLeft, this.guiTop);
        for (final GuiElement<?> element : ((List<GuiElement<?>>) this.elements)) {
            element.drawBackground(this.guiLeft, this.guiTop);
        }

    }

    @Override
    public void setRecipe(
            final IRecipeLayout layout,
            final AirColRecipeWrapper recipes,
            @Nonnull final IIngredients ingredients
    ) {


        IGuiFluidStackGroup fff = layout.getFluidStacks();

        fff.init(0, false, 24, 24, 12, 47, 10000, true, null);
        fff.set(0, recipes.getOutputs().get(0));

        fff.init(1, false, 64, 24, 12, 47, 10000, true, null);
        fff.set(1, recipes.getOutputs().get(1));


        fff.init(2, false, 104, 24, 12, 47, 10000, true, null);
        fff.set(2, recipes.getOutputs().get(2));


    }

    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guiaircollector.png");
    }


}
