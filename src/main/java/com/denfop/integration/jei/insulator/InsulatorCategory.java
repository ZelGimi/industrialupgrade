package com.denfop.integration.jei.insulator;

import com.denfop.Constants;
import com.denfop.api.widget.EnumTypeComponent;
import com.denfop.api.widget.ScreenWidget;
import com.denfop.api.widget.TankWidget;
import com.denfop.api.widget.WidgetDefault;
import com.denfop.blockentity.mechanism.BlockEntityFluidIntegrator;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
import com.denfop.componets.ComponentProgress;
import com.denfop.componets.ComponentRenderInventory;
import com.denfop.componets.EnumTypeComponentSlot;
import com.denfop.containermenu.ContainerMenuFluidIntegrator;
import com.denfop.integration.jei.IRecipeCategory;
import com.denfop.integration.jei.JEICompat;
import com.denfop.integration.jei.JeiInform;
import com.denfop.screen.ScreenMain;
import com.denfop.utils.Localization;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.List;

public class InsulatorCategory extends ScreenMain implements IRecipeCategory<InsulatorHandler> {

    private final IDrawableStatic bg;
    private final JeiInform jeiInform;
    private final ContainerMenuFluidIntegrator container1;
    private final ScreenWidget slots1;
    private final ScreenWidget progress_bar;
    private int progress;


    public InsulatorCategory(
            IGuiHelper guiHelper, JeiInform jeiInform
    ) {
        super(((BlockEntityFluidIntegrator) BlockBaseMachine3Entity.fluid_integrator.getDummyTe()).getGuiContainer(Minecraft.getInstance().player));
        this.jeiInform = jeiInform;
        this.title = net.minecraft.network.chat.Component.literal(getTitles());
        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine" +
                        ".png"), 3, 3, 140,
                107
        );
        this.componentList.clear();
        this.slots = new ScreenWidget(this, 3, 3, getComponent(),
                new WidgetDefault<>(new ComponentRenderInventory(EnumTypeComponentSlot.SLOTS__JEI_INPUT))
        );
        this.slots1 = new ScreenWidget(this, 3, 3, getComponent(),
                new WidgetDefault<>(new ComponentRenderInventory(EnumTypeComponentSlot.SLOTS__JEI_OUTPUT))
        );
        this.container1 = (ContainerMenuFluidIntegrator) this.getContainer();
        progress_bar = new ScreenWidget(this, 70 + 30, 35, EnumTypeComponent.PROCESS,
                new WidgetDefault<>(new ComponentProgress(this.container1.base, 1, (short) 100))
        );
        this.componentList.add(progress_bar);
        this.addWidget(TankWidget.createNormal(this, 10, 17, container1.base.fluidTank1));
        this.addWidget(TankWidget.createNormal(this, 46, 17, container1.base.fluidTank2));
    }


    @Nonnull
    @Override
    public String getTitles() {
        return Localization.translate((JEICompat.getBlockStack(BlockBaseMachine3Entity.insulator)).getDescriptionId());
    }


    @Nonnull
    @Override
    public IDrawable getBackground() {
        return bg;
    }

    @Override
    public void draw(InsulatorHandler recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        progress++;
        double xScale = progress / 100D;
        if (xScale >= 1) {
            progress = 0;
        }
        this.slots.drawBackground(stack, 10, 0);
        this.slots1.drawBackground(stack, 5, 0);

        progress_bar.renderBar(stack, -10, 10, xScale);
        for (final ScreenWidget element : ((List<ScreenWidget>) this.elements)) {
            element.drawBackground(stack, this.guiLeft, this.guiTop);
        }
    }

    @Override
    public RecipeType<InsulatorHandler> getRecipeType() {
        return jeiInform.recipeType;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, InsulatorHandler recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 14, 21).setFluidRenderer(10000, true, 12, 47).addFluidStack(recipe.getInputFluid().getFluid(), recipe.getInputFluid().getAmount());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 50, 21).setFluidRenderer(10000, true, 12, 47).addFluidStack(recipe.getOutputFluid().getFluid(), recipe.getOutputFluid().getAmount());
        builder.addSlot(RecipeIngredientRole.INPUT, 60 + 10, 44).addItemStack(recipe.getInput());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 115 + 5, 44).addItemStack(recipe.getOutput());
    }


    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }


}
