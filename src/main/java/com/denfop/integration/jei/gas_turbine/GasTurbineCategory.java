package com.denfop.integration.jei.gas_turbine;

import com.denfop.Constants;
import com.denfop.blockentity.mechanism.BlockEntityOilRefiner;
import com.denfop.blocks.mechanism.BlockGasTurbineEntity;
import com.denfop.blocks.mechanism.BlockRefinerEntity;
import com.denfop.integration.jei.IRecipeCategory;
import com.denfop.integration.jei.JEICompat;
import com.denfop.integration.jei.JeiInform;
import com.denfop.screen.ScreenMain;
import com.denfop.utils.Localization;
import com.denfop.utils.ModUtils;
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

public class GasTurbineCategory extends ScreenMain implements IRecipeCategory<GasTurbineHandler> {

    private final IDrawableStatic bg;
    private final JeiInform jeiInform;

    public GasTurbineCategory(
            final IGuiHelper guiHelper, JeiInform jeiInform
    ) {
        super(((BlockEntityOilRefiner) BlockRefinerEntity.refiner.getDummyTe()).getGuiContainer(Minecraft.getInstance().player));
        this.jeiInform = jeiInform;
        this.title = net.minecraft.network.chat.Component.literal(getTitles());

        bg = guiHelper.createDrawable(ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/NeutronGeneratorGUI".toLowerCase() +
                        ".png"), 5, 5, 140,
                75
        );
    }


    @Nonnull
    @Override
    public String getTitles() {
        return JEICompat.getBlockStack(BlockGasTurbineEntity.gas_turbine_controller).getDisplayName().getString();
    }

    @Override
    public RecipeType<GasTurbineHandler> getRecipeType() {
        return jeiInform.recipeType;
    }

    @Nonnull
    @Override
    public IDrawable getBackground() {
        return bg;
    }

    @Override
    public void draw(GasTurbineHandler recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics stack, double mouseX, double mouseY) {
        drawSplitString(stack,
                Localization.translate("iu.windgenerator1"),
                5,
                30,
                150 - 10,
                4210752
        );
        drawSplitString(stack,
                ModUtils.getString(recipe.getEnergy()),
                5,
                30 + 10,
                150 - 10,
                4210752
        );
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, GasTurbineHandler recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 95, 21).setFluidRenderer(10000, true, 12, 47).addFluidStack(recipe.getOutput().getFluid(), recipe.getOutput().getAmount());
    }


    protected ResourceLocation getTexture() {
        return ResourceLocation.tryBuild(Constants.MOD_ID, "textures/gui/NeutronGeneratorGUI.png");
    }


}
