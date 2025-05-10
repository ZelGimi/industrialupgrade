package com.denfop.integration.jei.incubator;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.Component;
import com.denfop.api.gui.EnumTypeComponent;
import com.denfop.api.gui.GuiComponent;
import com.denfop.api.gui.GuiElement;
import com.denfop.api.gui.TankGauge;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.ComponentProgress;
import com.denfop.componets.ComponentRenderInventory;
import com.denfop.componets.EnumTypeComponentSlot;
import com.denfop.container.ContainerSingleFluidAdapter;
import com.denfop.gui.GuiIU;
import com.denfop.integration.jei.IRecipeCategory;
import com.denfop.integration.jei.JEICompat;
import com.denfop.integration.jei.JeiInform;
import com.denfop.tiles.mechanism.TileEntitySingleFluidAdapter;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.forge.ForgeTypes;
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

public class IncubatorCategory extends GuiIU implements IRecipeCategory<IncubatorHandler> {

    private final IDrawableStatic bg;
    private final ContainerSingleFluidAdapter container1;
    private final GuiComponent slots1;
    private final GuiComponent progress_bar;
    private int progress;

    private final JeiInform jeiInform;
    public IncubatorCategory(
            IGuiHelper guiHelper, JeiInform jeiInform
    ) {
        super(((TileEntitySingleFluidAdapter) BlockBaseMachine3.single_fluid_adapter.getDummyTe()).getGuiContainer(Minecraft.getInstance().player));
        this.jeiInform=jeiInform;
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
        this.container1 = (ContainerSingleFluidAdapter) this.getContainer();
        progress_bar = new GuiComponent(this, 70, 35, EnumTypeComponent.PROCESS,
                new Component<>(new ComponentProgress(this.container1.base, 1, (short) 100))
        );
        this.componentList.add(progress_bar);
        this.addElement(TankGauge.createNormal(this, 10, 17, container1.base.fluidTank1));
        this.addElement(TankGauge.createNormal(this, 46 + 71, 17, container1.base.fluidTank2));
    }


    @Nonnull
    @Override
    public String getTitles() {
        return Localization.translate((JEICompat.getBlockStack(BlockBaseMachine3.incubator)).getDescriptionId());
    }


    @Nonnull
    @Override
    public IDrawable getBackground() {
        return bg;
    }

    @Override
    public void draw(IncubatorHandler recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        progress++;
        double xScale = progress / 100D;
        if (xScale >= 1) {
            progress = 0;
        }
        this.slots.drawBackground( stack, -20, 0);
        progress_bar.renderBar( stack, 20, 10, xScale);
        for (final GuiElement<?> element : ((List<GuiElement<?>>) this.elements)) {
            element.drawBackground( stack, this.guiLeft, this.guiTop);
        }
    }

    @Override
    public RecipeType<IncubatorHandler> getRecipeType() {
        return jeiInform.recipeType;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, IncubatorHandler recipe, IFocusGroup focuses) {


            builder.addSlot(RecipeIngredientRole.INPUT, 14, 21)
                    .setFluidRenderer(10000, true, 12, 47)
                    .addFluidStack( recipe.getInputFluid().getFluid(), recipe.getInputFluid().getAmount());


            builder.addSlot(RecipeIngredientRole.OUTPUT, 121, 21)
                    .setFluidRenderer(10000, true, 12, 47)
                    .addFluidStack( recipe.getOutputFluid().getFluid(),recipe.getOutputFluid().getAmount());


            builder.addSlot(RecipeIngredientRole.INPUT, 55, 44)
                    .addItemStack(recipe.getInput());
        }



    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }


}
