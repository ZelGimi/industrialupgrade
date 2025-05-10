package com.denfop.integration.jei.smeltery_controller;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.gui.GuiElement;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.blocks.mechanism.BlockSmeltery;
import com.denfop.componets.ComponentProgress;
import com.denfop.componets.ComponentRenderInventory;
import com.denfop.componets.EnumTypeComponentSlot;
import com.denfop.container.ContainerFluidIntegrator;
import com.denfop.gui.GuiIU;
import com.denfop.integration.jei.IRecipeCategory;
import com.denfop.integration.jei.JEICompat;
import com.denfop.integration.jei.JeiInform;
import com.denfop.tiles.mechanism.TileEntityFluidIntegrator;
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

public class SmelteryControllerCategory extends GuiIU implements IRecipeCategory<SmelteryControllerHandler> {

    private final IDrawableStatic bg;
    private final ContainerFluidIntegrator container1;
    private final GuiComponent slots1;
    private final GuiComponent progress_bar;
    private int progress;
    JeiInform jeiInform;

    public SmelteryControllerCategory(
            IGuiHelper guiHelper, JeiInform jeiInform
    ) {
        super(((TileEntityFluidIntegrator) BlockBaseMachine3.fluid_integrator.getDummyTe()).getGuiContainer(Minecraft.getInstance().player));
        this.jeiInform = jeiInform;
        this.title = net.minecraft.network.chat.Component.literal(getTitles());
        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine" +
                        ".png"), 3, 3, 140,
                107
        );
        this.componentList.clear();
        this.slots = new GuiComponent(this, 3, 3, getComponent(),
                new Component<>(new ComponentRenderInventory(EnumTypeComponentSlot.SLOTS__JEI_INPUT))
        );
        this.slots1 = new GuiComponent(this, 3, 3, getComponent(),
                new Component<>(new ComponentRenderInventory(EnumTypeComponentSlot.SLOTS__JEI_OUTPUT))
        );
        this.container1 = (ContainerFluidIntegrator) this.getContainer();
        progress_bar = new GuiComponent(this, 70, 35, EnumTypeComponent.PROCESS,
                new Component<>(new ComponentProgress(this.container1.base, 1, (short) 100))
        );
        this.componentList.add(progress_bar);
    }

    @Override
    public RecipeType<SmelteryControllerHandler> getRecipeType() {
        return jeiInform.recipeType;
    }

    @Nonnull
    @Override
    public String getTitles() {
        return Localization.translate((JEICompat.getBlockStack(BlockSmeltery.smeltery_controller)).getDescriptionId());
    }


    @Nonnull
    @Override
    public IDrawable getBackground() {
        return bg;
    }

    @Override
    public void draw(SmelteryControllerHandler recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        progress++;
        double xScale = progress / 100D;
        if (xScale >= 1) {
            progress = 0;
        }

        progress_bar.renderBar(stack,-10, 10, xScale);
        for (final GuiElement<?> element : ((List<GuiElement<?>>) this.elements)) {
            element.drawBackground(stack,this.guiLeft, this.guiTop);
        }
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, SmelteryControllerHandler recipe, IFocusGroup focuses) {
        for (int i = 0; i < recipe.getInput().size(); i++) {
            int yDisplayPosition1 = 60 + (30 - recipe.getInput().size()) * i - recipe.getInput().size() * 16;
            builder.addSlot(RecipeIngredientRole.INPUT,  60 - 21, yDisplayPosition1).setFluidRenderer( recipe.getInput().get(i).getAmount(),true,16, 16).addFluidStack(recipe.getInput().get(i).getFluid(),recipe.getInput().get(i).getAmount());
        }
        builder.addSlot(RecipeIngredientRole.OUTPUT, 85, 44).setFluidRenderer(recipe.getOutputFluid().getAmount(),true,16, 16).addFluidStack(recipe.getOutputFluid().getFluid(),recipe.getOutputFluid().getAmount());

    }


    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }


}
