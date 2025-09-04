package com.denfop.integration.jei.gas_well;

import com.denfop.Constants;
import com.denfop.blockentity.mechanism.BlockEntityOilRefiner;
import com.denfop.blocks.mechanism.BlockGasWellEntity;
import com.denfop.blocks.mechanism.BlockRefinerEntity;
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

public class GasWellCategory extends ScreenMain implements IRecipeCategory<GasWellHandler> {

    private final IDrawableStatic bg;
    private final JeiInform jeiInform;

    public GasWellCategory(
            final IGuiHelper guiHelper, JeiInform jeiInform
    ) {
        super(((BlockEntityOilRefiner) BlockRefinerEntity.refiner.getDummyTe()).getGuiContainer(Minecraft.getInstance().player));
        this.jeiInform = jeiInform;
        this.title = net.minecraft.network.chat.Component.literal(getTitles());
        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/common3" +
                        ".png"), 3, 3, 140,
                140
        );
    }


    @Nonnull
    @Override
    public String getTitles() {
        return Localization.translate(JEICompat.getBlockStack(BlockGasWellEntity.gas_well_controller).getDescriptionId());
    }

    @Override
    public RecipeType<GasWellHandler> getRecipeType() {
        return jeiInform.recipeType;
    }

    @Nonnull
    @Override
    public IDrawable getBackground() {
        return bg;
    }


    @Override
    public void draw(GasWellHandler recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        drawSplitString(stack, Localization.translate(recipe.output), 5, 3,
                140 - 5, 4210752
        );

    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, GasWellHandler recipe, IFocusGroup focuses) {
        builder.addInvisibleIngredients(RecipeIngredientRole.OUTPUT).addFluidStack(recipe.input, 1);
    }


    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/guivein.png");
    }


}
