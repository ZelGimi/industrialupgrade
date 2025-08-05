package com.denfop.integration.jei.polymerizer;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.*;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.componets.ComponentProgress;
import com.denfop.container.ContainerImpOilRefiner;
import com.denfop.gui.GuiIU;
import com.denfop.integration.jei.IRecipeCategory;
import com.denfop.integration.jei.JEICompat;
import com.denfop.integration.jei.JeiInform;
import com.denfop.tiles.mechanism.TileEntityPolymerizer;
import com.denfop.tiles.mechanism.TileImpOilRefiner;
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

public class PolymerizerCategory extends GuiIU implements IRecipeCategory<PolymerizerHandler> {

    private final IDrawableStatic bg;
    private final ContainerImpOilRefiner container1;
    private final GuiComponent progress_bar;
    private final JeiInform jeiInform;
    private int progress = 0;
    private int energy = 0;

    public PolymerizerCategory(
            IGuiHelper guiHelper, JeiInform jeiInform
    ) {
        super(((TileImpOilRefiner) BlockBaseMachine3.imp_refiner.getDummyTe()).getGuiContainer(Minecraft.getInstance().player));

        bg = guiHelper.createDrawable(ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guimachine" +
                        ".png"), 3, 3, 140,
                77
        );
        this.jeiInform = jeiInform;
        this.title = net.minecraft.network.chat.Component.literal(getTitles());
        this.componentList.clear();
        this.container1 = (ContainerImpOilRefiner) this.getContainer();
        progress_bar = new GuiComponent(this, 70, 35, EnumTypeComponent.PROCESS,
                new Component<>(new ComponentProgress(this.container1.base, 1, (short) 100))
        );
        this.componentList.add(progress_bar);
        this.addElement(TankGauge.createNormal(this, 40, 4,
                ((TileEntityPolymerizer) BlockBaseMachine3.polymerizer.getDummyTe()).fluidTank1
        ));

        this.addElement(TankGauge.createNormal(this, 100, 4,
                ((TileEntityPolymerizer) BlockBaseMachine3.polymerizer.getDummyTe()).fluidTank2
        ));

    }


    @Nonnull
    @Override
    public String getTitles() {
        return Localization.translate(JEICompat.getBlockStack(BlockBaseMachine3.polymerizer).getDescriptionId());
    }

    @Override
    public RecipeType<PolymerizerHandler> getRecipeType() {
        return jeiInform.recipeType;
    }

    @Nonnull
    @Override
    public IDrawable getBackground() {
        return bg;
    }

    @Override
    public void draw(PolymerizerHandler recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics stack, double mouseX, double mouseY) {
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

        for (final GuiElement<?> element : ((List<GuiElement<?>>) this.elements)) {
            element.drawBackground(stack, this.guiLeft, this.guiTop);
        }
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, PolymerizerHandler recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 44, 8).setFluidRenderer(10000, true, 12, 47).addFluidStack(recipe.getInput().getFluid(), recipe.getInput().getAmount());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 104, 8).setFluidRenderer(10000, true, 12, 47).addFluidStack(recipe.getOutput().getFluid(), recipe.getOutput().getAmount());

    }


    protected ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guimachine.png");
    }


}
