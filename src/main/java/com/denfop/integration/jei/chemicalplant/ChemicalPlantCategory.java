package com.denfop.integration.jei.chemicalplant;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.*;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.blocks.mechanism.BlockChemicalPlant;
import com.denfop.componets.ComponentProgress;
import com.denfop.container.ContainerImpOilRefiner;
import com.denfop.gui.GuiIU;
import com.denfop.integration.jei.IRecipeCategory;
import com.denfop.integration.jei.JEICompat;
import com.denfop.integration.jei.JeiInform;
import com.denfop.tiles.base.TileEntityRefrigeratorFluids;
import com.denfop.tiles.mechanism.TileImpOilRefiner;
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

public class ChemicalPlantCategory extends GuiIU implements IRecipeCategory<ChemicalPlantHandler> {

    private final IDrawableStatic bg;
    private final ContainerImpOilRefiner container1;
    private final GuiComponent progress_bar;
    private int progress = 0;
    private int energy = 0;
    JeiInform jeiInform;
    public ChemicalPlantCategory(
            final IGuiHelper guiHelper, JeiInform jeiInform
    ) {
        super(((TileImpOilRefiner) BlockBaseMachine3.imp_refiner.getDummyTe()).getGuiContainer(Minecraft.getInstance().player));
        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine" +
                        ".png"), 3, 3, 140,
                77
        );
        this.jeiInform=jeiInform;
        this.title = net.minecraft.network.chat.Component.literal(getTitles());
        this.componentList.clear();
        this.container1 = (ContainerImpOilRefiner) this.getContainer();
        progress_bar = new GuiComponent(this, 70, 35, EnumTypeComponent.PROCESS,
                new Component<>(new ComponentProgress(this.container1.base, 1, (short) 100))
        );
        this.componentList.add(progress_bar);
        this.addElement(TankGauge.createNormal(this, 40, 4,
                ((TileEntityRefrigeratorFluids) BlockBaseMachine3.refrigerator_fluids.getDummyTe()).fluidTank1
        ));

        this.addElement(TankGauge.createNormal(this, 100, 4,
                ((TileEntityRefrigeratorFluids) BlockBaseMachine3.refrigerator_fluids.getDummyTe()).fluidTank2
        ));

    }


    @Nonnull
    @Override
    public String getTitles() {
        return Localization.translate(JEICompat.getBlockStack(BlockChemicalPlant.chemical_plant_controller).getDescriptionId());
    }


    @Override
    public RecipeType<ChemicalPlantHandler> getRecipeType() {
        return jeiInform.recipeType;
    }

    @Nonnull
    @Override
    public IDrawable getBackground() {
        return bg;
    }

    @Override
    public void draw(ChemicalPlantHandler recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
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

        progress_bar.renderBar( stack,0, 0, xScale);

        for (final GuiElement<?> element : ((List<GuiElement<?>>) this.elements)) {
            element.drawBackground( stack,this.guiLeft, this.guiTop);
        }
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ChemicalPlantHandler recipes, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT,44, 8).setFluidRenderer(8000,true,12,47).addFluidStack(recipes.getInput().getFluid(),recipes.getInput().getAmount());
        builder.addSlot(RecipeIngredientRole.OUTPUT,104, 8).setFluidRenderer(8000,true,12,47).addFluidStack(recipes.getOutput().getFluid(),recipes.getOutput().getAmount());
    }


    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine.png");
    }


}
