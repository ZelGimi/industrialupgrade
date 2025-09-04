package com.denfop.integration.jei.fluidheater;

import com.denfop.Constants;
import com.denfop.api.widget.EnumTypeComponent;
import com.denfop.api.widget.ScreenWidget;
import com.denfop.api.widget.TankWidget;
import com.denfop.api.widget.WidgetDefault;
import com.denfop.blockentity.base.BlockEntityRefrigeratorFluids;
import com.denfop.blockentity.mechanism.BlockEntityImpOilRefiner;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.componets.ComponentProgress;
import com.denfop.containermenu.ContainerMenuImpOilRefiner;
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

public class HeaterFluidsCategory extends ScreenMain implements IRecipeCategory<HeaterFluidsHandler> {

    private final IDrawableStatic bg;
    private final ContainerMenuImpOilRefiner container1;
    private final ScreenWidget progress_bar;
    private final JeiInform jeiInform;
    private int progress = 0;
    private int energy = 0;

    public HeaterFluidsCategory(
            final IGuiHelper guiHelper, JeiInform jeiInform
    ) {
        super(((BlockEntityImpOilRefiner) BlockBaseMachine3Entity.imp_refiner.getDummyTe()).getGuiContainer(Minecraft.getInstance().player));
        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine" +
                        ".png"), 3, 3, 140,
                77
        );
        this.jeiInform = jeiInform;
        this.title = net.minecraft.network.chat.Component.literal(getTitles());
        this.componentList.clear();
        this.container1 = (ContainerMenuImpOilRefiner) this.getContainer();
        progress_bar = new ScreenWidget(this, 70, 35, EnumTypeComponent.PROCESS,
                new WidgetDefault<>(new ComponentProgress(this.container1.base, 1, (short) 100))
        );
        this.componentList.add(progress_bar);
        this.addWidget(TankWidget.createNormal(this, 40, 4,
                ((BlockEntityRefrigeratorFluids) BlockBaseMachine3Entity.refrigerator_fluids.getDummyTe()).fluidTank1
        ));

        this.addWidget(TankWidget.createNormal(this, 100, 4,
                ((BlockEntityRefrigeratorFluids) BlockBaseMachine3Entity.refrigerator_fluids.getDummyTe()).fluidTank2
        ));

    }


    @Nonnull
    @Override
    public String getTitles() {
        return Localization.translate(JEICompat.getBlockStack(BlockBaseMachine3Entity.fluid_heater).getDescriptionId());
    }


    @Nonnull
    @Override
    public IDrawable getBackground() {
        return bg;
    }

    @Override
    public void draw(HeaterFluidsHandler recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics stack, double mouseX, double mouseY) {
        progress++;
        if (this.energy < 100) {
            energy++;
        }
        double energylevel = energy / 100D;
        double xScale = progress / 100D;
        if (xScale >= 1) {
            progress = 0;
        }
        bindTexture(getTexture());

        progress_bar.renderBar(stack, 0, 0, xScale);

        for (final ScreenWidget element : ((List<ScreenWidget>) this.elements)) {
            element.drawBackground(stack, this.guiLeft, this.guiTop);
        }

    }

    @Override
    public RecipeType<HeaterFluidsHandler> getRecipeType() {
        return jeiInform.recipeType;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, HeaterFluidsHandler recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 44, 8).setFluidRenderer(8000, true, 12, 47).addFluidStack(recipe.getInput().getFluid(), recipe.getInput().getAmount());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 104, 8).setFluidRenderer(8000, true, 12, 47).addFluidStack(recipe.getOutput().getFluid(), recipe.getOutput().getAmount());

    }


    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }


}
