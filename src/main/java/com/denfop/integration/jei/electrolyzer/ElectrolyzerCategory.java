package com.denfop.integration.jei.electrolyzer;

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
import com.denfop.blocks.mechanism.BlockBaseMachine2;
import com.denfop.gui.GuiIU;
import com.denfop.tiles.mechanism.TileAdvOilRefiner;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IGuiFluidStackGroup;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.List;

public class ElectrolyzerCategory extends GuiIU implements IRecipeCategory<ElectrolyzerRecipeWrapper> {

    private final IDrawableStatic bg;
    private int energy = 0;

    public ElectrolyzerCategory(
            final IGuiHelper guiHelper
    ) {
        super(((TileAdvOilRefiner) BlockAdvRefiner.adv_refiner.getDummyTe()).getGuiContainer(Minecraft.getMinecraft().player));

        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine" +
                        ".png"), 3, 3, 140,
                77
        );
        this.componentList.clear();
        this.addElement(TankGauge.createNormal(this, 12, 20, ((TileAdvOilRefiner) container.base).getFluidTank(0)));
        this.addElement(TankGauge.createNormal(this, 60, 20, ((TileAdvOilRefiner) container.base).getFluidTank(1)));
        this.addElement(TankGauge.createNormal(this, 108, 20, ((TileAdvOilRefiner) container.base).getFluidTank(2)));

    }

    @Nonnull
    @Override
    public String getUid() {
        return BlockBaseMachine2.electrolyzer_iu.getName();
    }

    @Nonnull
    @Override
    public String getTitle() {
        return Localization.translate(new ItemStack(IUItem.basemachine1, 1, 15).getUnlocalizedName());
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
            final ElectrolyzerRecipeWrapper recipes,
            @Nonnull final IIngredients ingredients
    ) {

        IGuiItemStackGroup isg = layout.getItemStacks();

        IGuiFluidStackGroup fff = layout.getFluidStacks();

        fff.init(0, true, 16, 24, 12, 47, 10000, true, null);
        fff.set(0, recipes.getInput());

        fff.init(1, false, 16 + 48, 24, 12, 47, 10000, true, null);
        fff.set(1, recipes.getOutputs().get(0));


        fff.init(2, false, 16 + 96, 24, 12, 47, 10000, true, null);
        fff.set(2, recipes.getOutputs().get(1));

        isg.init(3, true, 13 + 48, 2);

        isg.set(3, new ItemStack(IUItem.cathode));
        isg.init(4, true, 13 + 96, 2);

        isg.set(4, new ItemStack(IUItem.anode));

    }

    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/GUIElectolyzer.png");
    }


}
