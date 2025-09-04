package com.denfop.integration.jei.gas_chamber;

import com.denfop.Constants;
import com.denfop.api.widget.*;
import com.denfop.blockentity.mechanism.BlockEntityOilRefiner;
import com.denfop.blocks.mechanism.BlockGasChamberEntity;
import com.denfop.blocks.mechanism.BlockRefinerEntity;
import com.denfop.integration.jei.IRecipeCategory;
import com.denfop.integration.jei.JEICompat;
import com.denfop.integration.jei.JeiInform;
import com.denfop.screen.ScreenMain;
import com.denfop.utils.Localization;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.List;

public class GasChamberCategory extends ScreenMain implements IRecipeCategory<GasChamberHandler> {

    private final IDrawableStatic bg;
    private final JeiInform jeiInform;


    public GasChamberCategory(
            final IGuiHelper guiHelper, JeiInform jeiInform
    ) {
        super(((BlockEntityOilRefiner) BlockRefinerEntity.refiner.getDummyTe()).getGuiContainer(Minecraft.getInstance().player));
        this.jeiInform = jeiInform;
        this.title = net.minecraft.network.chat.Component.literal(getTitles());
        bg = guiHelper.createDrawable(ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guimachine" +
                        ".png"), 3, 3, 140,
                77
        );
        this.componentList.clear();
        this.addWidget(TankWidget.createNormal(this, 12, 20, ((BlockEntityOilRefiner) container.base).fluidTank1));
        this.addWidget(TankWidget.createNormal(this, 60, 20, ((BlockEntityOilRefiner) container.base).fluidTank2));
        this.addWidget(TankWidget.createNormal(this, 108, 20, ((BlockEntityOilRefiner) container.base).fluidTank2));

    }


    @Nonnull
    @Override
    public String getTitles() {
        return Localization.translate((JEICompat.getBlockStack(BlockGasChamberEntity.primal_gas_chamber)).getDescriptionId());
    }


    @Nonnull
    @Override
    public IDrawable getBackground() {
        return bg;
    }

    @Override
    public void draw(GasChamberHandler recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics stack, double mouseX, double mouseY) {
        new ScreenWidget(this, 88, 40, EnumTypeComponent.FLUID_PART1,
                new WidgetDefault<>(new EmptyWidget())
        ).drawBackground(stack, this.guiLeft, this.guiTop);
        new ScreenWidget(this, 35, 38, EnumTypeComponent.PLUS_BUTTON,
                new WidgetDefault<>(new EmptyWidget())
        ).drawBackground(stack, this.guiLeft, this.guiTop);
        for (final ScreenWidget element : ((List<ScreenWidget>) this.elements)) {
            element.drawBackground(stack, this.guiLeft, this.guiTop);
        }
    }

    @Override
    public RecipeType<GasChamberHandler> getRecipeType() {
        return jeiInform.recipeType;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, GasChamberHandler recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 16, 24).setFluidRenderer(10000, true, 12, 47).addFluidStack(recipe.getInput().getFluid(), recipe.getInput().getAmount());
        builder.addSlot(RecipeIngredientRole.INPUT, 16 + 48, 24).setFluidRenderer(10000, true, 12, 47).addFluidStack(recipe.getOutput().getFluid(), recipe.getOutput().getAmount());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 16 + 96, 24).setFluidRenderer(10000, true, 12, 47).addFluidStack(recipe.getOutput1().getFluid(), recipe.getOutput1().getAmount());

    }


    protected ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guioilrefiner.png");
    }


}
