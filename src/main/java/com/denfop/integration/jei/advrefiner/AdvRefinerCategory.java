package com.denfop.integration.jei.advrefiner;

import com.denfop.Constants;
import com.denfop.IUItem;
import com.denfop.api.widget.*;
import com.denfop.blockentity.mechanism.BlockEntityAdvOilRefiner;
import com.denfop.blocks.mechanism.BlockAdvRefinerEntity;
import com.denfop.integration.jei.IRecipeCategory;
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
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;

public class AdvRefinerCategory extends ScreenMain implements IRecipeCategory<AdvRefinerHandler> {

    private final IDrawableStatic bg;
    private final JeiInform<AdvRefinerCategory, AdvRefinerHandler> jeiInform;
    private int energy = 0;

    public AdvRefinerCategory(
            final IGuiHelper guiHelper, JeiInform<AdvRefinerCategory, AdvRefinerHandler> jeiInform
    ) {
        super(((BlockEntityAdvOilRefiner) BlockAdvRefinerEntity.adv_refiner.getDummyTe()).getGuiContainer(Minecraft.getInstance().player));
        this.jeiInform = jeiInform;
        this.title = net.minecraft.network.chat.Component.literal(getTitles());
        bg = guiHelper.createDrawable(ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guimachine" +
                        ".png"), 3, 3, 140,
                77
        );
        this.componentList.clear();
        this.addWidget(TankWidget.createNormal(this, 12, 20, ((BlockEntityAdvOilRefiner) container.base).getFluidTank(0)));
        this.addWidget(TankWidget.createNormal(this, 60, 20, ((BlockEntityAdvOilRefiner) container.base).getFluidTank(1)));
        this.addWidget(TankWidget.createNormal(this, 108, 20, ((BlockEntityAdvOilRefiner) container.base).getFluidTank(2)));

    }


    @Override
    public RecipeType<AdvRefinerHandler> getRecipeType() {
        return jeiInform.recipeType;
    }

    @Nonnull
    @Override
    public String getTitles() {
        return Localization.translate(new ItemStack(IUItem.oiladvrefiner.getItem()).getDescriptionId());
    }

    @SuppressWarnings("removal")
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
    public void setRecipe(IRecipeLayoutBuilder builder, AdvRefinerHandler recipes, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 16, 24).setFluidRenderer(10000, true, 12, 47).addFluidStack(recipes.getInput().getFluid(), recipes.getInput().getAmount());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 16 + 48, 24).setFluidRenderer(10000, true, 12, 47).addFluidStack(recipes.getOutput().getFluid(), recipes.getOutput().getAmount());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 16 + 96, 24).setFluidRenderer(10000, true, 12, 47).addFluidStack(recipes.getOutput1().getFluid(), recipes.getOutput1().getAmount());
    }


    @Override
    public void draw(AdvRefinerHandler recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics stack, double mouseX, double mouseY) {
        new ScreenWidget(this, 35, 38, EnumTypeComponent.FLUID_PART1,
                new WidgetDefault<>(new EmptyWidget())
        ).drawBackground(stack, this.guiLeft, this.guiTop);
        new ScreenWidget(this, 88, 40, EnumTypeComponent.PLUS_BUTTON,
                new WidgetDefault<>(new EmptyWidget())
        ).drawBackground(stack, this.guiLeft, this.guiTop);
        for (final ScreenWidget element : ((List<ScreenWidget>) this.elements)) {
            element.drawBackground(stack, this.guiLeft, this.guiTop);
        }
    }


    protected ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/guioilrefiner.png");
    }


}
