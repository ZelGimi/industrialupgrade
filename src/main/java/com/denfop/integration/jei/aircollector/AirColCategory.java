package com.denfop.integration.jei.aircollector;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.Localization;
import com.denfop.api.gui.*;
import com.denfop.blocks.mechanism.BlockAdvRefiner;
import com.denfop.gui.GuiIU;
import com.denfop.integration.jei.IRecipeCategory;
import com.denfop.integration.jei.JeiInform;
import com.denfop.recipes.ItemStackHelper;
import com.denfop.tiles.mechanism.TileAdvOilRefiner;
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

public class AirColCategory extends GuiIU implements IRecipeCategory<AirColHandler> {

    private final IDrawableStatic bg;
    private int energy = 0;
    private final JeiInform<AirColCategory, AirColHandler>jeiInform;
    public AirColCategory(
            final IGuiHelper guiHelper, JeiInform<AirColCategory, AirColHandler> jeiInform
    ) {
        super(((TileAdvOilRefiner) BlockAdvRefiner.adv_refiner.getDummyTe()).getGuiContainer(Minecraft.getInstance().player));
        this.jeiInform=jeiInform;
        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/guimachine" +
                        ".png"), 3, 3, 140,
                77
        );
        this.componentList.clear();
        this.addElement(TankGauge.createNormal(this, 20, 20, ((TileAdvOilRefiner) container.base).getFluidTank(0)));
        this.addElement(TankGauge.createNormal(this, 60, 20, ((TileAdvOilRefiner) container.base).getFluidTank(1)));
        this.addElement(TankGauge.createNormal(this, 100, 20, ((TileAdvOilRefiner) container.base).getFluidTank(2)));

        this.title = net.minecraft.network.chat.Component.literal(getTitles());
    }


    @Override
    public RecipeType<AirColHandler> getRecipeType() {
        return jeiInform.recipeType;
    }

    @Nonnull
    @Override
    public String getTitles() {
        return Localization.translate(ItemStackHelper.fromData(IUItem.basemachine2, 1, 11).getDescriptionId());
    }



    @Nonnull
    @Override
    public IDrawable getBackground() {
        return bg;
    }

    @Override
    public IDrawable getIcon() {
        return null;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, AirColHandler recipes, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.OUTPUT,24, 24).setFluidRenderer(10000, true,12, 47).addFluidStack(recipes.getOutput().getFluid(),recipes.getOutput().getAmount());
        builder.addSlot(RecipeIngredientRole.OUTPUT,64, 24).setFluidRenderer(10000, true,12, 47).addFluidStack(recipes.getOutput1().getFluid(),recipes.getOutput1().getAmount());
        builder.addSlot(RecipeIngredientRole.OUTPUT,104, 24).setFluidRenderer(10000, true,12, 47).addFluidStack(recipes.getOutput2().getFluid(),recipes.getOutput2().getAmount());
    }

    @Override
    public void draw(AirColHandler recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {

        new GuiComponent(this, 44, 40, EnumTypeComponent.PLUS_BUTTON,
                new Component<>(new ComponentEmpty())
        ).drawBackground(stack, this.guiLeft, this.guiTop);
        new GuiComponent(this, 84, 40, EnumTypeComponent.PLUS_BUTTON,
                new Component<>(new ComponentEmpty())
        ).drawBackground(stack, this.guiLeft, this.guiTop);
        for (final GuiElement<?> element : ((List<GuiElement<?>>) this.elements)) {
            element.drawBackground(stack, this.guiLeft, this.guiTop);
        }
    }




    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guiaircollector.png");
    }


}
