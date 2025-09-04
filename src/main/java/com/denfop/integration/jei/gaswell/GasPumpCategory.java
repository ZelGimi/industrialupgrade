package com.denfop.integration.jei.gaswell;

import com.denfop.Constants;
import com.denfop.blockentity.mechanism.BlockEntityOilRefiner;
import com.denfop.blocks.mechanism.BlockBaseMachine3Entity;
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

public class GasPumpCategory extends ScreenMain implements IRecipeCategory<GasPumpHandler> {

    private final IDrawableStatic bg;
    private final JeiInform jeiInform;

    public GasPumpCategory(
            final IGuiHelper guiHelper, JeiInform jeiInform
    ) {
        super(((BlockEntityOilRefiner) BlockRefinerEntity.refiner.getDummyTe()).getGuiContainer(Minecraft.getInstance().player));
        this.jeiInform = jeiInform;
        this.title = net.minecraft.network.chat.Component.literal(getTitles());
        bg = guiHelper.createDrawable(new ResourceLocation(Constants.MOD_ID, "textures/gui/NeutronGeneratorGUI".toLowerCase() +
                        ".png"), 5, 5, 140,
                75
        );
    }


    @Nonnull
    @Override
    public String getTitles() {
        return Localization.translate(JEICompat.getBlockStack(BlockBaseMachine3Entity.gas_pump).getDescriptionId());
    }

    @Override
    public RecipeType<GasPumpHandler> getRecipeType() {
        return jeiInform.recipeType;
    }

    @Nonnull
    @Override
    public IDrawable getBackground() {
        return bg;
    }

    @Override
    public void draw(GasPumpHandler recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        drawSplitString(stack,
                Localization.translate("iu.gaspump.info"),
                10,
                5,
                140 - 10,
                4210752
        );
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, GasPumpHandler recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.OUTPUT, 95, 21).setFluidRenderer(10000, true, 12, 47).addFluidStack(recipe.getOutput().getFluid(), recipe.getOutput().getAmount());

    }


    protected ResourceLocation getTexture() {
        return new ResourceLocation(Constants.MOD_ID, "textures/gui/NeutronGeneratorGUI.png".toLowerCase());
    }


}
