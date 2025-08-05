package com.denfop.integration.jei.genetic_replicator;

import com.denfop.Constants;
import com.denfop.Localization;
import com.denfop.api.gui.*;
import com.denfop.blocks.mechanism.BlockBaseMachine3;
import com.denfop.blocks.mechanism.BlockRefiner;
import com.denfop.gui.GuiIU;
import com.denfop.integration.jei.IRecipeCategory;
import com.denfop.integration.jei.JEICompat;
import com.denfop.integration.jei.JeiInform;
import com.denfop.tiles.mechanism.TileOilRefiner;
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

public class GeneticReplicatorCategory extends GuiIU implements IRecipeCategory<GeneticReplicatorHandler> {

    private final IDrawableStatic bg;
    private final JeiInform jeiInform;


    public GeneticReplicatorCategory(
            IGuiHelper guiHelper, JeiInform jeiInform
    ) {
        super(((TileOilRefiner) BlockRefiner.refiner.getDummyTe()).getGuiContainer(Minecraft.getInstance().player));
        this.jeiInform = jeiInform;
        this.title = net.minecraft.network.chat.Component.literal(getTitles());
        bg = guiHelper.createDrawable(ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guimachine" +
                        ".png"), 3, 3, 140,
                77
        );
        this.componentList.clear();
        this.addElement(TankGauge.createNormal(this, 12, 20, ((TileOilRefiner) container.base).fluidTank1));
        this.addElement(TankGauge.createNormal(this, 60, 20, ((TileOilRefiner) container.base).fluidTank2));
        this.addElement(TankGauge.createNormal(this, 108, 20, ((TileOilRefiner) container.base).fluidTank2));

    }

    @Override
    public RecipeType<GeneticReplicatorHandler> getRecipeType() {
        return jeiInform.recipeType;
    }


    @Nonnull
    @Override
    public String getTitles() {
        return Localization.translate((JEICompat.getBlockStack(BlockBaseMachine3.genetic_replicator)).getDescriptionId());
    }


    @Nonnull
    @Override
    public IDrawable getBackground() {
        return bg;
    }

    @Override
    public void draw(GeneticReplicatorHandler recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics stack, double mouseX, double mouseY) {
        new GuiComponent(this, 88, 40, EnumTypeComponent.FLUID_PART1,
                new Component<>(new ComponentEmpty())
        ).drawBackground(stack, this.guiLeft, this.guiTop);
        new GuiComponent(this, 35, 38, EnumTypeComponent.PLUS_BUTTON,
                new Component<>(new ComponentEmpty())
        ).drawBackground(stack, this.guiLeft, this.guiTop);
        for (final GuiElement<?> element : ((List<GuiElement<?>>) this.elements)) {
            element.drawBackground(stack, this.guiLeft, this.guiTop);
        }
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, GeneticReplicatorHandler recipes, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 16, 24).setFluidRenderer(12000, true, 12, 47).addFluidStack(recipes.getInput().getFluid(), recipes.getInput().getAmount());
        builder.addSlot(RecipeIngredientRole.INPUT, 16 + 48, 24).setFluidRenderer(12000, true, 12, 47).addFluidStack(recipes.getOutput().getFluid(), recipes.getOutput().getAmount());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 16 + 96, 24).setFluidRenderer(12000, true, 12, 47).addFluidStack(recipes.getOutput1().getFluid(), recipes.getOutput1().getAmount());

    }


    protected ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guioilrefiner.png");
    }


}
